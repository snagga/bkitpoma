using System;

using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GPSUtils;
using Com.BkitMobile.Poma.Mobile.Api.Geometry;
using Com.BkitMobile.Poma.Mobile.Api.Connection;
using Com.BkitMobile.Poma.Mobile.Api.Operation;

namespace PomaMobile
{
    public partial class PomaMobile : Form
    {
        Gps gps = null;
        GpsPosition position = null;
        GpsDeviceState device = null;
        private EventHandler UpdateGpsDataHandler = null;

        DeviceSession session = null;
        delegate void UpdateSessionDelegate(long id, string pass);
        void updateSession(long id, string pass)
        {
            if (session == null) return;
            if (!InvokeRequired)
            {
                session.DeviceID = id;
                session.Password = pass;
            }
            else
            {
                Invoke(new UpdateSessionDelegate(updateSession), id, pass);
            }
        }

        public PomaMobile()
        {
            InitializeComponent();
        }


        private void PomaMobile_Load(object sender, EventArgs e)
        {
            //Internet.Init();
            session = new DeviceSession();
            //txtURL.Text = Operation<object>.SERVICE_URL = "http://localhost:8080/api/mobile";
            gps = new Gps();
            gps.DeviceStateChanged += new DeviceStateChangedEventHandler(gps_DeviceStateChanged);
            gps.LocationChanged += new LocationChangedEventHandler(gps_LocationChanged);
            UpdateGpsDataHandler = new EventHandler(UpdateGpsData);
        }

        void gps_LocationChanged(object sender, LocationChangedEventArgs args)
        {
            position = args.Position;

            // call the UpdateData method via the updateDataHandler so that we
            // update the UI on the UI thread
            try
            {
                Invoke(UpdateGpsDataHandler);
            }
            catch { }
        }

        void gps_DeviceStateChanged(object sender, DeviceStateChangedEventArgs args)
        {
            device = args.DeviceState;

            UpdateGpsDeviceState();

            try
            {
                Invoke(UpdateGpsDataHandler);
            }
            catch { }
        }

        private delegate void UpdateGpsDeviceStateDelegate();
        private void UpdateGpsDeviceState()
        {
            if (this.InvokeRequired)
            {
                Invoke(new UpdateGpsDeviceStateDelegate(UpdateGpsDeviceState));
            }
            else
            {
                string state = string.Empty;
                switch (device.DeviceState)
                {
                    case GpsServiceState.Off:
                        state = "Off";
                        break;
                    case GpsServiceState.On:
                        state = "On";
                        break;
                    case GpsServiceState.ShuttingDown:
                        state = "ShuttingDown";
                        break;
                    case GpsServiceState.StartingUp:
                        state = "StartingUp";
                        break;
                    case GpsServiceState.Uninitialized:
                        state = "Uninitialized";
                        break;
                    case GpsServiceState.Unknown:
                        state = "Unknown";
                        break;
                    case GpsServiceState.Unloading:
                        state = "Unloading";
                        break;
                }

                log(string.Format("GPS state: {0}", state));
            }
        }

        bool done = true;
        void UpdateGpsData(object sender, System.EventArgs args)
        {
            if (gps.Opened)
            {

                if (position != null && position.LatitudeValid && position.LongitudeValid)
                {
                    if (position.SatellitesInSolutionValid &&
                        position.SatellitesInViewValid &&
                        position.SatelliteCountValid)
                    {
                        lbGpsState.Text = string.Format("Satellite(s): {0}", position.SatelliteCount);
                    }
                    string s = string.Format("{0};{1}", position.Latitude.ToString("0.0000"), position.Longitude.ToString("0.0000"));
                    lbGeocode.Text = s;

                    if (done)
                    {
                        if (session.Schedule != null && !session.Schedule[(int)(DateTime.Now.Ticks / TimeSpan.TicksPerHour) % 24])
                        {
                            return;
                        }

                        done = false;
                        WaypointOperation result = null;
                        if (position.SpeedValid)
                            result = session.Waypoint(position.Latitude, position.Longitude, (long)position.Speed);
                        else result = session.Waypoint(position.Latitude, position.Longitude, 0);
                        if (result.OK)
                        {
                            log("Waypoint successed: " + result.Result);
                        }
                        else
                        {
                            log(result.Message);
                        }
                        done = true;
                    }
                }
            }
            else
            {
                log(string.Format("GPS state: {0}", device.DeviceState));
            }
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            if (gps != null && !gps.Opened) gps.Open();
            btnNewTrack_Click(null, null);
        }

        //private void btnLogin_Click(object sender, EventArgs e)
        //{
        //    FuncCallDelegate func = new FuncCallDelegate(_btnLogin_Click);
        //    func.BeginInvoke(null, null, null, null);
        //}

        private void btnLogin_Click(object sender, EventArgs e)
        {
            try
            {
                updateSession(long.Parse(txtID.Text), txtPWD.Text);
            }
            catch (Exception ex)
            {
                log(ex.Message);
            }

            LoginOperation result = session.Login();
            if (result.OK)
            {
                log("Login successed: " + result.Result);
            }
            else
            {
                log(result.Message);
            }

            ConfigOperation config = session.Config();
            if (config.OK)
            {
                session.Interval = config.Interval;
                session.Schedule = config.Schedule;

                log("Interval: " + config.Interval + " s");
                string s = "Schedule: ";
                foreach (bool b in config.Schedule)
                {
                    s += b + ",";
                }
                log(s);
            }
            else
            {
                log("Interval: " + config.RawMessage);
            }
        }

        delegate void FuncCallDelegate(object sender, EventArgs e);
        delegate void UpdateTextDelegate(string s);
        private void log(string s)
        {
            if (!InvokeRequired)
            {
                lock (txtConsole)
                {
                    txtConsole.Text += s + "\r\n";
                    txtConsole.SelectionStart = txtConsole.Text.Length;
                    txtConsole.ScrollToCaret();
                    txtConsole.Refresh();
                }
            }
            else
            {
                Invoke(new UpdateTextDelegate(log), s);
            }
        }

        private void PomaMobile_Closing(object sender, CancelEventArgs e)
        {
            if (gps != null && gps.Opened) gps.Close();
        }

        private void btnStop_Click(object sender, EventArgs e)
        {
            if (gps != null && gps.Opened) gps.Close();
        }

        private void txtURL_TextChanged(object sender, EventArgs e)
        {
            Operation<object>.SERVICE_URL = txtURL.Text;
        }

        //private void btnNewTrack_Click(object sender, EventArgs e)
        //{
        //    FuncCallDelegate func = new FuncCallDelegate(_btnNewTrack_Click);
        //    func.BeginInvoke(null, null, null, null);
        //}

        private void btnNewTrack_Click(object sender, EventArgs e)
        {
            NewTrackOperation result = session.NewTrack();
            if (result.OK)
            {
                log("New track ID: " + result.Result);
            }
            else
            {
                log(result.Message);
            }
        }
    }
}