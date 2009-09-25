using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// Result of Login operation
    /// </summary>
    public class LoginOperation : Operation<string>
    {
        public static string TRACKED_LOGIN
        {
            get
            {
                return Operation<object>.SERVICE_URL + "/";
            }
        }

        /// <summary>
        /// Device's ID
        /// </summary>
        public long TrackedID
        {
            get;
            set;
        }

        /// <summary>
        /// Device's Password
        /// </summary>
        public string Password
        {
            get;
            set;
        }

        public LoginOperation(HttpSessionConnection connection, long ID, string pass)
            : base(connection)
        {
            this.TrackedID = ID;
            this.Password = pass;
            parse(Execute());
        }

        protected override string Execute()
        {
            return Connection.HttpPost(TRACKED_LOGIN, string.Format("id={0}&pwd={1}", TrackedID, Password));
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
