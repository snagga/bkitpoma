using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;

namespace Com.BkitMobile.Poma.Mobile.Api.Connection
{
    /// <summary>
    /// Represents a session connection through HTTP Protocol
    /// </summary>
    public class HttpSessionConnection
    {
        private WebProxy __WebProxy;
        /// <summary>
        /// Proxy of this connection
        /// </summary>
        public string WebProxy
        {
            get
            {
                if (__WebProxy == null) return "";
                return __WebProxy.Address.ToString();
            }
            set
            {
                __WebProxy = new WebProxy(value);
            }
        }
        /// <summary>
        /// Session ID of this connection
        /// </summary>
        public string SessionID
        {
            get;
            set;
        }

        /// <summary>
        /// Do a HTTP GET
        /// </summary>
        /// <param name="uri"></param>
        /// <returns>Result of <code>uri</code></returns>
        public string HttpGet(string uri)
        {
            // parameters: name1=value1&name2=value2	
            WebRequest webRequest = null;
            try
            {
                webRequest = WebRequest.Create(uri);
            }
            catch (Exception ex) { return ex.Message; }
            if (__WebProxy != null) webRequest.Proxy = __WebProxy;

            webRequest.Method = "GET";
            webRequest.Timeout = 30000;
            if (SessionID != null)
            {
                webRequest.Headers["Cookie"] = SessionID;
            }

            try
            { // get the response
                using (WebResponse webResponse = webRequest.GetResponse())
                {
                    if (webResponse == null)
                    { return null; }

                    string cookie = webResponse.Headers["Set-Cookie"];
                    if (cookie != null)
                    {
                        int semicolon = cookie.IndexOf(';');
                        SessionID = cookie.Substring(0, semicolon);
                    }

                    using (StreamReader sr = new StreamReader(webResponse.GetResponseStream()))
                    {
                        string s = sr.ReadToEnd().Trim();
                        sr.Close();
                        webResponse.Close();
                        return s;
                    }
                }
            }
            catch (WebException ex)
            {
                return ex.Message;
            }
        } // end HttpPost 

        /// <summary>
        /// Do a HTTP POST
        /// </summary>
        /// <param name="uri"></param>
        /// <returns>Result of <code>uri</code></returns>
        public string HttpPost(string uri, string parameters)
        {
            // parameters: name1=value1&name2=value2
            WebRequest webRequest = null;

            try
            {
                webRequest = WebRequest.Create(uri);
            }
            catch (Exception ex) { return ex.Message; }

            if (__WebProxy != null) webRequest.Proxy = __WebProxy;

            webRequest.ContentType = "application/x-www-form-urlencoded";
            webRequest.Method = "POST";
            webRequest.Timeout = 30000;

            if (SessionID != null)
            {
                webRequest.Headers["Cookie"] = SessionID;
            }
            byte[] bytes = Encoding.ASCII.GetBytes(parameters);
            Stream os = null;
            try
            { // send the Post
                webRequest.ContentLength = bytes.Length;   //Count bytes to send
                os = webRequest.GetRequestStream();
                os.Write(bytes, 0, bytes.Length);         //Send it
                os.Close();
            }
            catch (WebException ex)
            {
                return ex.Message;
            }
            finally
            {
                try { os.Close(); }
                catch { }
            }

            try
            { // get the response
                using (WebResponse webResponse = webRequest.GetResponse())
                {
                    if (webResponse == null)
                    { return null; }

                    string cookie = webResponse.Headers["Set-Cookie"];
                    if (cookie != null)
                    {
                        int semicolon = cookie.IndexOf(';');
                        SessionID = cookie.Substring(0, semicolon);
                    }

                    using (StreamReader sr = new StreamReader(webResponse.GetResponseStream()))
                    {
                        string s = sr.ReadToEnd().Trim();
                        sr.Close();
                        webResponse.Close();
                        return s;
                    }
                }
            }
            catch (WebException ex)
            {
                return ex.Message;
            }
        } // end HttpPost 
    }
}
