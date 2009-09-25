using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// Logout operation
    /// </summary>
    public class LogoutOperation : Operation<string>
    {

        public static string DEVICE_LOGOUT
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/device/logout";
            }
        }

        public LogoutOperation(HttpSessionConnection connection)
            : base(connection)
        {
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpGet(DEVICE_LOGOUT);
        }

        protected override void parseResult()
        {
            //OK,username
            try
            {
                string[] s = RawMessage.Split(',');
                OK = s[0].Equals("OK");
                Result = s[1];
            }
            catch { OK = false; }
        }
    }
}
