using System;

using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// Represents an operation used to get Config
    /// </summary>
    public class ConfigOperation : Operation<bool[]>
    {
        /// <summary>
        /// URL used to get Config
        /// </summary>
        public static string DEVICE_CONFIG
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/device/config";
            }
        }

        /// <summary>
        /// Interval that device send location to POMA
        /// </summary>
        public long Interval
        {
            get;
            set;
        }

        /// <summary>
        /// Schedule that device send location to POMA
        /// </summary>
        public bool[] Schedule
        {
            get;
            set;
        }

        public ConfigOperation(HttpSessionConnection connection)
            : base(connection)
        {
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpGet(DEVICE_CONFIG);
        }

        protected override void parseResult()
        {
            // OK,interval,1.1.1.1.0.1..
            long[] res = new long[25];
            try
            {
                Schedule = new bool[24];
                string[] s = RawMessage.Replace("\r\n", "").Split(',');
                OK = s[0].Equals("OK");
                res[24] = long.Parse(s[1]);
                Interval = res[24];
                s = s[2].Split('.');
                for (int i = 0; i < 24; i++)
                {
                    res[i] = long.Parse(s[i]);
                    Schedule[i] = res[i] != 0;
                }
                Result = Schedule;
            }
            catch { OK = false; }
        }
    }
}
