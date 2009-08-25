package org.verisign.joid.consumer;

import org.verisign.joid.OpenIdException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: treeder
 * Date: Jul 17, 2007
 * Time: 5:05:52 PM
 */
public class Discoverer
{

    public ServerAndDelegate findIdServer(String identityUrl)
            throws Exception
    {


        ServerAndDelegate serverAndDelegate = new ServerAndDelegate();

        // OpenID 2.0, we first try to check with YADIS protocol
        findWithYadis(identityUrl, serverAndDelegate);

        if (serverAndDelegate.getServer() == null) {
            throw new OpenIdException("No openid.server found on identity page.");
        }
        return serverAndDelegate;
    }

    public void findWithYadis(String identityUrl, ServerAndDelegate serverAndDelegate) throws Exception
    {
        BufferedReader in = null;
        //HttpClient httpClient = new HttpClient(new SimpleHttpConnectionManager());
        //httpClient.getParams().setSoTimeout(15000);
        //httpClient.getParams().setConnectionManagerTimeout(15000);
        
        //GetMethod get = null;
        HttpURLConnection connection =null;
        try {

            System.out.println("identityUrl=" + identityUrl);
            //get = new GetMethod(identityUrl);
            // test
            connection = createConnection(identityUrl);
            int status = connection.getResponseCode();

            System.out.println("status="+status);
            Map<String,List<String>> headerFields = connection.getHeaderFields(); 
            dumpHeaders(headerFields);
//            System.out.println("body=" + get.getResponseBodyAsString());
            

            String contentType = connection.getHeaderField("Content-Type");
            if(contentType != null && contentType.contains("application/xrds+xml")){
                // then we're looking at the xrds service doc already
                XRDSDocument xrdsDocument = buildXrdsDocument(connection);
                handleXrdsDocument(serverAndDelegate, xrdsDocument);
                return;
            }

            String locationHeader = connection.getHeaderField("x-xrds-location"); 
            if (locationHeader != null) {
                System.out.println("found yadis header: " + locationHeader);
                XRDSDocument xrdsDocument = fetchYadisDocument(locationHeader);
                handleXrdsDocument(serverAndDelegate, xrdsDocument);
                return;
            } else {
                // try to find it in the HTML, OpenID 1.0 style
                // todo: should also look for X-XRDS-Location in a meta tag here
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                findServerAndDelegate(serverAndDelegate, in);
                return;
            }

            /*
            This is the last try for YADIS:
            get = new GetMethod(identityUrl);
            get.setRequestHeader("Accept", " application/xrds+xml");
            httpClient.executeMethod(get);

            Header contentType = get.getResponseHeader("content-type");
            System.out.println("content-type=" + contentType);
            get.releaseConnection();

             */
        } finally {
            if (in != null) in.close();
            if (connection != null) connection.disconnect();
        }
    }

    private void handleXrdsDocument(ServerAndDelegate serverAndDelegate, XRDSDocument xrdsDocument)
    {
        List<XRDSService> services = xrdsDocument.getServiceList();
        for (XRDSService service : services) {
            System.out.println("service=" + service.getUri());
            serverAndDelegate.setServer(service.getUri());
            //  todo: also sset delegate after we get it
        }
    }

    private void dumpHeaders(Map<String,List<String>> responseHeaders)
    {
    	Iterator<String> i = responseHeaders.keySet().iterator();
        while (i.hasNext() ) {
        	String header = i.next();
        	List<String> vals = responseHeaders.get(header);
            System.out.println(header + "=" + ((vals!=null&&vals.size()>0)?vals.get(0):""));
        }
    }

    private void findServerAndDelegate(ServerAndDelegate serverAndDelegate, BufferedReader in)
            throws IOException
    {
        String str;
        while ((str = in.readLine()) != null) {
            if (serverAndDelegate.getServer() == null) {
                serverAndDelegate.setServer(findLinkTag(str, "openid.server", in));
            }
            if (serverAndDelegate.getDelegate() == null) {
                serverAndDelegate.setDelegate(findLinkTag(str, "openid.delegate", in));
            }
            if (str.indexOf("</head>") >= 0) {
                break;
            }
        }
    }

    private XRDSDocument fetchYadisDocument( String location) throws IOException, ParserConfigurationException, SAXException
    {
        HttpURLConnection connection = null;
        XRDSDocument doc = null;
        try {
        	connection = createConnection(location);
	        connection.getResponseCode();
	        doc = buildXrdsDocument(connection);
        } catch (Exception e){
        	if(connection!=null)connection.disconnect();
        }
        return doc;
    }
    private HttpURLConnection createConnection(String url) throws IOException{
        URL identityURLObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) identityURLObject.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        return connection;
    }

    private XRDSDocument buildXrdsDocument(HttpURLConnection connection)
            throws ParserConfigurationException, SAXException, IOException
    {
        XRDSDocument doc = new XRDSDocument();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(connection.getInputStream());
        connection.disconnect();
        NodeList list = document.getElementsByTagName("Service");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            System.out.println("servicenode=" + node);
            NodeList childNodes = node.getChildNodes();
            XRDSService service = new XRDSService();
            for (int j = 0; j < childNodes.getLength(); j++) {
                // todo: ensure <Type> is http://openid.net/signon/1.0 - http://yadis.org/wiki/Yadis_1.0_(HTML)#7._The_Yadis_document
                // todo: get delegate <openid:Delegate>
                Node node2 = childNodes.item(j);
                System.out.println(node2.getNodeName());
                if (node2.getNodeName().equalsIgnoreCase("URI")) {
                    service.setUri(node2.getTextContent());
                }
            }
            doc.addService(service);
        }
        return doc;
    }


    private String findLinkTag(String str, String rel, BufferedReader in)
            throws IOException
    {
        int index = str.indexOf(rel);
        if (index != -1) {
            // todo: ensure it's a proper link tag
            // todo: allow multiple line tag
            // todo: allow reverse ordering
            String href = findHref(str, index);
            if (href == null) {
                // no href found, check next line
                str = in.readLine();
                if (str != null) {
                    href = findHref(str, 0);
                }
            }
            return href;
        }
        return null;
    }

    private String findHref(String str, int index)
    {
        String href = null;
        int indexOfHref = str.indexOf("href=", index);
        if (indexOfHref != -1) {
            href = str.substring(indexOfHref + 6, str.indexOf("\"", indexOfHref + 8));
        }
        return href;
    }
}
