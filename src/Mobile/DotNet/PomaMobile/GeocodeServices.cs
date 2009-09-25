using System;

using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using System.Runtime.InteropServices;

namespace PomaMobile
{
    public class Geocode
    {
        public Geocode(double lat, double lng)
        {
            Latitude = lat;
            Longitude = lng;
        }
        public double Latitude
        {
            get;
            set;
        }

        public double Longitude
        {
            get;
            set;
        }
    }

    public static class GeocodeServices
    {
        static void WriteString(BinaryWriter writer, string s)
        {
            byte[] buff = UTF8Encoding.Default.GetBytes(s);
            short len = (short)(buff.Length);
            WriteShort(writer, len);
            writer.Write(buff);
        }

        static void WriteShort(BinaryWriter writer, short s)
        {
            using (MemoryStream stream = new MemoryStream())
            {
                BinaryWriter otherWriter = new BinaryWriter(stream);
                otherWriter.Write(s);
                byte[] buff = stream.GetBuffer();
                List<byte> thing = new List<byte>(buff);
                thing.RemoveRange(sizeof(short), thing.Count - sizeof(short));
                thing.Reverse();
                writer.Write(thing.ToArray());
            }
        }

        static void WriteInt(BinaryWriter writer, int i)
        {
            using (MemoryStream stream = new MemoryStream())
            {
                BinaryWriter otherWriter = new BinaryWriter(stream);
                otherWriter.Write(i);
                byte[] buff = stream.GetBuffer();
                List<byte> thing = new List<byte>(buff);
                thing.RemoveRange(sizeof(int), thing.Count - sizeof(int));
                thing.Reverse();
                writer.Write(thing.ToArray());
            }
        }

        static int ReadInt(BinaryReader reader)
        {
            byte[] bytes = reader.ReadBytes(4);
            int ret = (int)bytes[0] << 24 | (int)bytes[1] << 16 | (int)bytes[2] << 8 | (int)bytes[3];
            return ret;
        }

        const int SPI_GETOEMINFO = 258;
        [DllImport("coredll.dll", SetLastError = true)]
        static extern int SystemParametersInfo(int uiAction, int uiParam, StringBuilder pvParam, int fWinIni);
        static string Model
        {
            get
            {
                try
                {
                    StringBuilder OEMInfo = new StringBuilder(100);
                    int details = SystemParametersInfo(SPI_GETOEMINFO, OEMInfo.Capacity, OEMInfo, 0);
                    return OEMInfo.ToString();
                }
                catch (Exception)
                {
                    return string.Empty;
                }
            }
        }

        public static Geocode GetGeocode(int cellID, int locationAreaCode)
        {
            HttpWebRequest req = HttpWebRequest.Create("http://www.google.com/glm/mmap") as HttpWebRequest;
            MemoryStream s = new MemoryStream();
            BinaryWriter writer = new BinaryWriter(s, UTF8Encoding.Default);

            WriteShort(writer, (short)21);
            writer.Write((long)0);
            WriteString(writer, "fr");
            writer.Flush();
            byte[] buff = s.GetBuffer();
            WriteString(writer, "something");
            WriteString(writer, "1.3.1");
            WriteString(writer, "Web");
            writer.Write((byte)27);

            writer.Write(0);
            writer.Write(0);
            WriteInt(writer, 3);
            WriteString(writer, "");
            WriteInt(writer, cellID);  // CELL-ID
            WriteInt(writer, locationAreaCode);     // LAC
            writer.Write(0);
            writer.Write(0);
            writer.Write(0);
            writer.Write(0);
            writer.Flush();
            byte[] shit = s.GetBuffer();

            req.Method = "POST";
            req.ContentType = "application/x-www-form-urlencoded";
            req.ContentLength = s.Length;
            Stream reqStream = req.GetRequestStream();
            reqStream.Write(shit, 0, (int)s.Length);
            reqStream.Close();
            using (HttpWebResponse resp = req.GetResponse() as HttpWebResponse)
            {
                using (BinaryReader reader = new BinaryReader(resp.GetResponseStream()))
                {
                    reader.ReadBytes(7);
                    double lat = (double)ReadInt(reader) / 1000000;
                    double lon = (double)ReadInt(reader) / 1000000;
                    return new Geocode(lat, lon);
                }
            }
        }
    }
}
