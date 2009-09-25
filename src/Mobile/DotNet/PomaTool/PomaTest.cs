using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Com.BkitMobile.Poma.Mobile.Api.Connection;
using Com.BkitMobile.Poma.Mobile.Api.Geometry;
using Com.BkitMobile.Poma.Mobile.Api.Operation;
using System.IO;
using System.Globalization;

namespace POMATest
{
    public partial class PomaTest : Form
    {
        DeviceSession session = null;
        Queue<Geocode> geocodeQueue;
        public PomaTest()
        {
            InitializeComponent();
            long id = 0L;
            try { id = long.Parse(txtUsername.Text); }
            catch { };

            session = new DeviceSession();
            txtServiceURL.Text = Operation<object>.SERVICE_URL;
            txtServiceURL.Text = "http://localhost:8080/api/mobile";
            geocodeQueue = new Queue<Geocode>();
        }

        delegate void FuncCallDelegate(object sender, EventArgs e);

        delegate void UpdateTextDelegate(string s);

        delegate void VoidCall();

        void output(string s)
        {
            if (InvokeRequired)
            {
                this.Invoke(new UpdateTextDelegate(output), s);
            }
            else
            {
                lock (txtOutput)
                {
                    txtOutput.AppendText(s + "\n");
                }
            }
        }

        void updateTrackID(string s)
        {
            if (InvokeRequired)
            {
                Invoke(new UpdateTextDelegate(updateTrackID), s);
            }
            else
            {
                lbTrackID.Text = s;
            }
        }

        delegate void InsertListGeocodeDelegate(int pos, object geo);

        private void btnInsert_Click(object sender, EventArgs e)
        {
            FuncCallDelegate func = new FuncCallDelegate(_btnInsert_Click);
            func.BeginInvoke(null, null, null, null);
        }

        private void _btnInsert_Click(object sender, EventArgs e)
        {
            foreach (string line in txtInput.Lines)
            {
                try
                {
                    Geocode geocode = new Geocode(line.Trim());
                    geocodeQueue.Enqueue(geocode);
                    Invoke(new InsertListGeocodeDelegate(listGeocode.Items.Insert), 0, geocode);
                }
                catch { }
            }
            Invoke(new VoidCall(txtInput.Clear));
        }

        private void timerInsert_Tick(object sender, EventArgs e)
        {
            if (isPause) return;
            FuncCallDelegate func = new FuncCallDelegate(_timerInsert_Tick);
            func.BeginInvoke(null, null, null, null);
        }

        delegate void ListGeocodeRemoveDelegate(int pos);

        bool done = true;
        long millisecond = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
        private void _timerInsert_Tick(object sender, EventArgs e)
        {
            if (done)
            {
                done = false;
                if (geocodeQueue.Count > 0 && listGeocode.Items.Count > 0)
                {
                    Geocode geocode = geocodeQueue.Dequeue();
                    Console.WriteLine("Milliseconds: " + (DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond));
                    long time = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond - millisecond;
                    NumberFormatInfo info = new NumberFormatInfo();
                    info.PercentDecimalSeparator = ".";
                    text.WriteLine(string.Format("\t<waypoint time=\"{0}\" latitude=\"{1}\" longitude=\"{2}\" altitude=\"{3}\" />", time, geocode.Latitude.ToString(info), geocode.Longitude.ToString(info), millisecond % 50));
                    Invoke(new ListGeocodeRemoveDelegate(listGeocode.Items.RemoveAt), listGeocode.Items.Count - 1);
                    try
                    {
                        WaypointOperation result = session.Waypoint(geocode);
                        if (result.OK)
                        {
                            output("Waypoint: " + result.Result);
                            updateTrackID(result.Result.TrackID + "");
                        }
                        else
                        {
                            output("Waypoint: failed : " + geocode);
                        }
                    }
                    catch (Exception ex)
                    {
                        output(ex.Message);
                    }
                    millisecond = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
                }
                done = true;
            }
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e)
        {
            timerInsert.Interval = (int)numericUpDown1.Value;
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {
            FuncCallDelegate func = new FuncCallDelegate(_btnLogin_Click);
            func.BeginInvoke(null, null, null, null);
        }

        private void _btnLogin_Click(object sender, EventArgs e)
        {

            try
            {
                session.DeviceID = long.Parse(txtUsername.Text);
                session.Password = txtPassword.Text;
                LoginOperation result = session.Login();
                if (result.OK)
                {
                    output("Login successed: " + result.Result);
                }
                else
                {
                    output("Login failed");
                }

                ConfigOperation config = session.Config();
                if (config.OK)
                {
                    UpdateInterval(config.Interval * 1000);
                    output("Interval: " + config.Interval + " s");
                    string s = "Schedule: ";
                    foreach (bool b in config.Schedule)
                    {
                        s += b + ",";
                    }
                    output(s);
                }
                else
                {
                    output("Interval: " + config.RawMessage);
                }

            }
            catch (Exception ex)
            {
                output(ex.Message);
            }
        }

        delegate void UpdateIntervalDelegate(decimal interval);
        void UpdateInterval(decimal interval)
        {
            if (InvokeRequired)
            {
                this.Invoke(new UpdateIntervalDelegate(UpdateInterval), interval);
            }
            else
            {
                numericUpDown1.Value = interval;
            }
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            txtOutput.Text = "";
        }

        private void btnNewTrack_Click(object sender, EventArgs e)
        {
            FuncCallDelegate func = new FuncCallDelegate(_btnNewTrack_Click);
            func.BeginInvoke(null, null, null, null);
        }

        private void _btnNewTrack_Click(object sender, EventArgs e)
        {
            try
            {
                NewTrackOperation result = session.NewTrack();
                if (result.OK)
                {
                    output("New track: " + result.Result);
                    updateTrackID(result.Result + "");
                }
                else
                {
                    output("New track: failed");
                }
            }
            catch (Exception ex)
            {
                output(ex.Message);
            }
        }

        private void txtServiceURL_TextChanged(object sender, EventArgs e)
        {
            Operation<object>.SERVICE_URL = txtServiceURL.Text;
        }

        bool isPause = false;
        private void btnPause_Click(object sender, EventArgs e)
        {
            isPause = !isPause;
            btnPause.Text = isPause ? "Pause" : "Start";
        }
        FileInfo fileInfo;
        StreamWriter text;
        private void PomaTest_Load(object sender, EventArgs e)
        {
            fileInfo = new FileInfo(string.Format("Waypoint{0}.xml", DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond));
            text = fileInfo.CreateText();
            text.WriteLine("<waypoints>");
        }

        private void PomaTest_FormClosing(object sender, FormClosingEventArgs e)
        {
            text.Write("</waypoints>");
            text.Close();
        }
    }
}
