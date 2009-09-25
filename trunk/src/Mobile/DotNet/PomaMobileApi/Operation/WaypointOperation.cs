using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;
using Com.BkitMobile.Poma.Mobile.Api.Geometry;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// Waypoint operation
    /// </summary>
    public class WaypointOperation : Operation<Waypoint>
    {
        public static string WAYPOINT_LATLNGSPD
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/waypoint/latlng/{0}/{1}/{2}";
            }
        }

        /// <summary>
        /// Latitude
        /// </summary>
        public double Latitude
        {
            get;
            set;
        }

        /// <summary>
        /// Longitude
        /// </summary>
        public double Longitude
        {
            get;
            set;
        }

        /// <summary>
        /// Speed
        /// </summary>
        public long Speed
        {
            get;
            set;
        }

        public WaypointOperation(HttpSessionConnection connection, double latitude, double longitude)
            : this(connection, latitude, longitude, -1)
        {
        }

        public WaypointOperation(HttpSessionConnection connection, double latitude, double longitude, long speed)
            : base(connection)
        {
            this.Latitude = latitude;
            this.Longitude = longitude;
            this.Speed = speed;
            parse(Execute());
        }

        protected override string Execute()
        {
            NumberFormatInfo info = new NumberFormatInfo();
            info.PercentDecimalSeparator = ".";
            return Connection.HttpGet(string.Format(info, WAYPOINT_LATLNGSPD, Latitude, Longitude, Speed));
        }

        protected override void parseResult()
        {
            //OK,lat,lng,speed,time,trackID
            try
            {
                string[] s = RawMessage.Split(',');
                OK = s[0].Equals("OK");

                //Make sure that number in English format
                NumberFormatInfo info = new NumberFormatInfo();
                info.PercentDecimalSeparator = ".";

                double lat = double.Parse(s[1], info);
                double.Parse(s[1], info);
                double lng = double.Parse(s[2], info);

                long speed = long.Parse(s[3]);
                long time = long.Parse(s[4]);

                long trackID = long.Parse(s[5]);

                //Result a waypoint that inserted into Server Database
                Result = new Waypoint(new Geocode(lat, lng), speed, time, trackID);
            }
            catch { OK = false; }
        }
    }
}
