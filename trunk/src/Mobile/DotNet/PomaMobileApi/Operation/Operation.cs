using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Connection;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// Represents a operation of device
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public abstract class Operation<T>
    {
        public static string SERVICE_URL = "http://bkitpoma.appspot.com/api/mobile";

        /// <summary>
        /// Create, do a operation and parse a result from message returned by server
        /// </summary>
        /// <param name="sessionID">Session ID of operation's connection</param>
        public Operation(HttpSessionConnection connection)
        {
            this.Connection = connection;
        }

        /// <summary>
        /// Execute this operation
        /// </summary>
        protected abstract string Execute();

        /// <summary>
        /// Session Connection that this operation use to connect to server
        /// </summary>
        public HttpSessionConnection Connection
        {
            get;
            set;
        }

        /// <summary>
        /// Determines this result is successed or failed
        /// </summary>
        public bool OK
        {
            get;
            set;
        }

        /// <summary>
        /// The raw message returned by server
        /// </summary>
        public string RawMessage
        {
            get;
            set;
        }

        /// <summary>
        /// The message returned, usually be the same with Input message
        /// </summary>
        public string Message
        {
            get;
            set;
        }

        /// <summary>
        /// Object that tracked want to retrieve from server
        /// </summary>
        public T Result
        {
            get;
            set;
        }

        /// <summary>
        /// Pre-parse raw message returned by server
        /// </summary>
        protected void parse(string input)
        {
            RawMessage = input;
            OK = false;
            Message = RawMessage;
            if (RawMessage != null)
            {
                OK = RawMessage.StartsWith("OK");
                parseResult();
            }
        }

        /// <summary>
        /// Parse raw message returned by server
        /// </summary>
        protected abstract void parseResult();
    }
}
