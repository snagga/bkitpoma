using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;
using Com.BkitMobile.Poma.Mobile.Api.Geometry;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// CellID operation
    /// </summary>
    public class CellIDOperation : Operation<Waypoint>
    {
        public static string WAYPOINT_CELLID
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/waypoint/cellid/{0}/{1}/{2}/{3}";
            }
        }

        /// <summary>
        /// Cell's ID
        /// </summary>
        public int CellID
        {
            get;
            set;
        }

        /// <summary>
        /// Location Area Code
        /// </summary>
        public int LocationAreaCode
        {
            get;
            set;
        }

        /// <summary>
        /// Mobile Network Code
        /// </summary>
        public int MobileNetworkCode
        {
            get;
            set;
        }

        /// <summary>
        /// Mobile Country Code
        /// </summary>
        public int MobileCountryCode
        {
            get;
            set;
        }

        public CellIDOperation(HttpSessionConnection connection, int cellid, int lac)
            : this(connection, cellid, lac, 0, 0)
        {
        }

        public CellIDOperation(HttpSessionConnection connection, int cellid, int lac, int mnc, int mcc)
            : base(connection)
        {
            this.CellID = cellid;
            this.LocationAreaCode = lac;
            this.MobileNetworkCode = mnc;
            this.MobileCountryCode = mcc;
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpGet(string.Format(WAYPOINT_CELLID, CellID, LocationAreaCode, MobileNetworkCode, MobileCountryCode));
        }

        protected override void parseResult()
        {
            //OK,lat,lng,speed,time,deviceID
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
