using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// ChangePass operation
    /// </summary>
    public class ChangePassOperation : Operation<string>
    {

        public static string DEVICE_CHANGEPASS
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/device/changepass/{0}";
            }
        }

        /// <summary>
        /// New password of device
        /// </summary>
        public string NewPassword
        {
            get;
            set;
        }

        public ChangePassOperation(HttpSessionConnection connection, string newPass)
            : base(connection)
        {
            this.NewPassword = newPass;
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpGet(string.Format(DEVICE_CHANGEPASS, NewPassword));
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