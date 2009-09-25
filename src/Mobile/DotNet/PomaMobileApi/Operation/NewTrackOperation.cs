using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// NewTrack operation
    /// </summary>
    public class NewTrackOperation : Operation<long>
    {
        public static string TRACK_NEWTRACK
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/track/newtrack";
            }
        }

        public NewTrackOperation(HttpSessionConnection connection)
            : base(connection)
        {
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpGet(TRACK_NEWTRACK);
        }

        protected override void parseResult()
        {
            //OK,trackID
            try
            {
                string[] s = RawMessage.Split(',');
                OK = s[0].Equals("OK");
                Result = long.Parse(s[1]);
            }
            catch { OK = false; }
        }
    }
}
