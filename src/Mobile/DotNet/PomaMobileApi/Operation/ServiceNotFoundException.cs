using System;
using System.Collections.Generic;
using System.Text;

namespace Com.BkitMobile.Poma.Mobile.Api.Operation
{
    /// <summary>
    /// This exception will raised when have no internet connection or device session not yet certificated
    /// </summary>
    public class ServiceNotFoundException: Exception
    {
        public ServiceNotFoundException(string message)
            : base("Service Not Found Exception:" + message)
        { }
    }
}
