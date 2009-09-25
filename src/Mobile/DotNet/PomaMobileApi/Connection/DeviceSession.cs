using System;
using System.Collections.Generic;
using System.Text;
using Com.BkitMobile.Poma.Mobile.Api.Operation;
using Com.BkitMobile.Poma.Mobile.Api.Connection;
using Com.BkitMobile.Poma.Mobile.Api.Geometry;

namespace Com.BkitMobile.Poma.Mobile.Api.Connection
{
    /// <summary>
    /// Represents a working session of device.
    /// </summary>
    public class DeviceSession
    {
        /// <summary>
        /// Device's ID
        /// </summary>
        public long DeviceID
        {
            get;
            set;
        }

        private string __Password;
        /// <summary>
        /// Unencoded Password of device
        /// </summary>
        public string Password
        {
            set
            {
                __Password = value;
            }
        }

        /// <summary>
        /// Device's Interval
        /// </summary>
        public long Interval
        {
            get;
            set;
        }

        /// <summary>
        /// Device's Schedule
        /// </summary>
        public bool[] Schedule
        {
            get;
            set;
        }

        /// <summary>
        /// Session connection of device. Device use this connection to connect to POMA's server
        /// </summary>
        public HttpSessionConnection Connection
        {
            get;
            set;
        }

        /// <summary>
        /// Create a DeviceSession instance with specified device's ID and password
        /// </summary>
        /// <param name="ID">Device's ID</param>
        /// <param name="password">Device's password</param>
        public DeviceSession(long ID, string password)
        {
            this.DeviceID = ID;
            this.__Password = password;
            Connection = new HttpSessionConnection();
        }

        public DeviceSession()
        {
            this.DeviceID = 0L;
            this.__Password = "";
            Connection = new HttpSessionConnection();
        }

        /// <summary>
        /// Verify device session
        /// </summary>
        /// <returns></returns>
        public LoginOperation Login()
        {
            return new LoginOperation(Connection, DeviceID, __Password);
        }

        /// <summary>
        /// Remove device verified information
        /// </summary>
        /// <returns></returns>
        public LogoutOperation Logout()
        {
            return new LogoutOperation(Connection);
        }

        /// <summary>
        /// Change device's password
        /// </summary>
        /// <param name="newPassword"></param>
        /// <returns></returns>
        public ChangePassOperation ChangePass(string newPassword)
        {
            return new ChangePassOperation(Connection, newPassword);
        }

        /// <summary>
        /// Create a new track
        /// </summary>
        /// <returns></returns>
        public NewTrackOperation NewTrack()
        {
            return new NewTrackOperation(Connection);
        }

        /// <summary>
        /// Update device's location
        /// </summary>
        /// <param name="lat">Latitude</param>
        /// <param name="lng">Longitude</param>
        /// <returns></returns>
        public WaypointOperation Waypoint(double lat, double lng)
        {
            return Waypoint(lat, lng, -1);
        }

        /// <summary>
        /// Update device's location
        /// </summary>
        /// <param name="lat">Latitude</param>
        /// <param name="lng">Longitude</param>
        /// <param name="speed">Speed</param>
        /// <returns></returns>
        public WaypointOperation Waypoint(double lat, double lng, long speed)
        {
            return new WaypointOperation(Connection, lat, lng, speed);
        }

        /// <summary>
        /// Update device's location
        /// </summary>
        /// <param name="geocode">A geocode instance contains latitude, longitude of device</param>
        /// <returns></returns>
        public WaypointOperation Waypoint(Geocode geocode)
        {
            return Waypoint(geocode, -1);
        }

        /// <summary>
        /// Update device's location
        /// </summary>
        /// <param name="geocode">A geocode instance contains latitude, longitude of device</param>
        /// <param name="speed">Speed</param>
        /// <returns></returns>
        public WaypointOperation Waypoint(Geocode geocode, long speed)
        {
            return Waypoint(geocode.Latitude, geocode.Longitude, speed);
        }

        /// <summary>
        /// Update device's location through cellular techique
        /// </summary>
        /// <param name="cellid">CellID of this device's mobile</param>
        /// <param name="lac">Location Area Code (LAC) of this device's mobile</param>
        /// <returns></returns>
        public CellIDOperation Waypoint(int cellid, int lac)
        {
            return Waypoint(cellid, lac, 0, 0);
        }

        public CellIDOperation Waypoint(int cellid, int lac, int mnc, int mcc)
        {
            return new CellIDOperation(Connection, cellid, lac, mnc, mcc);
        }

        /// <summary>
        /// Get device's configuration
        /// </summary>
        /// <returns></returns>
        public ConfigOperation Config()
        {
            return new ConfigOperation(Connection);
        }
    }
}
