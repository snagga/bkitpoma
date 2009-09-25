namespace PomaMobile
{
    partial class PomaMobile
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.txtConsole = new System.Windows.Forms.TextBox();
            this.btnStart = new System.Windows.Forms.Button();
            this.btnStop = new System.Windows.Forms.Button();
            this.txtID = new System.Windows.Forms.TextBox();
            this.txtPWD = new System.Windows.Forms.TextBox();
            this.btnLogin = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.btnNewTrack = new System.Windows.Forms.Button();
            this.lbGpsState = new System.Windows.Forms.Label();
            this.txtURL = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.lbGeocode = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // txtConsole
            // 
            this.txtConsole.Location = new System.Drawing.Point(4, 103);
            this.txtConsole.Multiline = true;
            this.txtConsole.Name = "txtConsole";
            this.txtConsole.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.txtConsole.Size = new System.Drawing.Size(233, 188);
            this.txtConsole.TabIndex = 0;
            // 
            // btnStart
            // 
            this.btnStart.Location = new System.Drawing.Point(124, 3);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(56, 20);
            this.btnStart.TabIndex = 1;
            this.btnStart.Text = "Start";
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // btnStop
            // 
            this.btnStop.Location = new System.Drawing.Point(186, 3);
            this.btnStop.Name = "btnStop";
            this.btnStop.Size = new System.Drawing.Size(51, 20);
            this.btnStop.TabIndex = 2;
            this.btnStop.Text = "Stop";
            this.btnStop.Click += new System.EventHandler(this.btnStop_Click);
            // 
            // txtID
            // 
            this.txtID.Location = new System.Drawing.Point(33, 76);
            this.txtID.Name = "txtID";
            this.txtID.Size = new System.Drawing.Size(56, 21);
            this.txtID.TabIndex = 3;
            // 
            // txtPWD
            // 
            this.txtPWD.Location = new System.Drawing.Point(130, 76);
            this.txtPWD.Name = "txtPWD";
            this.txtPWD.Size = new System.Drawing.Size(56, 21);
            this.txtPWD.TabIndex = 4;
            // 
            // btnLogin
            // 
            this.btnLogin.Location = new System.Drawing.Point(192, 77);
            this.btnLogin.Name = "btnLogin";
            this.btnLogin.Size = new System.Drawing.Size(45, 20);
            this.btnLogin.TabIndex = 5;
            this.btnLogin.Text = "Login";
            this.btnLogin.Click += new System.EventHandler(this.btnLogin_Click);
            // 
            // label1
            // 
            this.label1.Location = new System.Drawing.Point(4, 79);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(23, 18);
            this.label1.Text = "ID";
            // 
            // label2
            // 
            this.label2.Location = new System.Drawing.Point(95, 79);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(29, 18);
            this.label2.Text = "Pass";
            // 
            // btnNewTrack
            // 
            this.btnNewTrack.Location = new System.Drawing.Point(166, 26);
            this.btnNewTrack.Name = "btnNewTrack";
            this.btnNewTrack.Size = new System.Drawing.Size(71, 20);
            this.btnNewTrack.TabIndex = 9;
            this.btnNewTrack.Text = "New track";
            this.btnNewTrack.Click += new System.EventHandler(this.btnNewTrack_Click);
            // 
            // lbGpsState
            // 
            this.lbGpsState.Location = new System.Drawing.Point(5, 3);
            this.lbGpsState.Name = "lbGpsState";
            this.lbGpsState.Size = new System.Drawing.Size(113, 20);
            // 
            // txtURL
            // 
            this.txtURL.Location = new System.Drawing.Point(33, 49);
            this.txtURL.Name = "txtURL";
            this.txtURL.Size = new System.Drawing.Size(204, 21);
            this.txtURL.TabIndex = 13;
            this.txtURL.TextChanged += new System.EventHandler(this.txtURL_TextChanged);
            // 
            // label3
            // 
            this.label3.Location = new System.Drawing.Point(3, 52);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(29, 18);
            this.label3.Text = "URL";
            // 
            // lbGeocode
            // 
            this.lbGeocode.Location = new System.Drawing.Point(5, 26);
            this.lbGeocode.Name = "lbGeocode";
            this.lbGeocode.Size = new System.Drawing.Size(155, 20);
            // 
            // PomaMobile
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(240, 294);
            this.Controls.Add(this.lbGeocode);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.txtURL);
            this.Controls.Add(this.lbGpsState);
            this.Controls.Add(this.btnNewTrack);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.btnLogin);
            this.Controls.Add(this.txtPWD);
            this.Controls.Add(this.txtID);
            this.Controls.Add(this.btnStop);
            this.Controls.Add(this.btnStart);
            this.Controls.Add(this.txtConsole);
            this.Name = "PomaMobile";
            this.Text = "POMA Mobile";
            this.Load += new System.EventHandler(this.PomaMobile_Load);
            this.Closing += new System.ComponentModel.CancelEventHandler(this.PomaMobile_Closing);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox txtConsole;
        private System.Windows.Forms.Button btnStart;
        private System.Windows.Forms.Button btnStop;
        private System.Windows.Forms.TextBox txtID;
        private System.Windows.Forms.TextBox txtPWD;
        private System.Windows.Forms.Button btnLogin;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnNewTrack;
        private System.Windows.Forms.Label lbGpsState;
        private System.Windows.Forms.TextBox txtURL;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label lbGeocode;


    }
}

