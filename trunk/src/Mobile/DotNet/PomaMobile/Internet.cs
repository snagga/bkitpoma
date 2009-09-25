using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.IO;

namespace PomaMobile
{
    public class Internet
    {
        public static Timer _timer;
        static bool _isInited = false;

        public static void Init()
        {
            if (!_isInited)
            {
                _timer = new Timer();
                _timer.Interval = 10000;
                _timer.Tick += new EventHandler(_timer_Tick);
                _timer.Enabled = true;
                _isInited = true;
            }
        }

        public static void _timer_Tick(object sender, EventArgs e)
        {
            try
            {
                System.Net.Dns.GetHostEntry("www.google.com");
                ConnectedToInternet = true;
            }
            catch
            {
                ConnectedToInternet = false;
            }
        }

        public static bool ConnectedToInternet
        {
            get;
            set;
        }

        public static String GetFile(string strURL)
        {

            WebRequest myWebRequest = WebRequest.Create(strURL);

            WebResponse myWebResponse = myWebRequest.GetResponse();

            Stream ReceiveStream = myWebResponse.GetResponseStream();

            Encoding encode = System.Text.Encoding.GetEncoding("utf-8");

            StreamReader readStream = new StreamReader(ReceiveStream, encode);

            string strResponse = readStream.ReadToEnd();

            readStream.Close();

            myWebResponse.Close();

            return strResponse;
        }
    }
}
