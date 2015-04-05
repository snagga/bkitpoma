<font size='4'>GUIDE CONFIGURATION TO COMPILE AND DEPLOY</font><br />
<font size='5'>BKITPOMA</font>

---

<br />
<font size='3'> Mục lục </font>


# Requirements: #
## Install Eclipse: ##
- Follow the following link to get latest Eclipse version [http://www.eclipse.org/downloads/](http://www.eclipse.org/downloads/)
<font size='4'><br /><u>Note</u></font>: The Google Plugin for Eclipse is available for Eclipse 3.3 (Europa), Eclipse 3.4 (Ganymede) and Eclipse 3.5 (Galileo). The "Eclipse IDE for Java EE Developers" includes all of the components you will need for web application development.

## Install Google  Web Toolkit plug-in for Eclipse: ##
You can install the Google Plugin for Eclipse using the Software Update feature of Eclipse.

To install the plugin, using “Eclipse 3.5 (Galileo)”:

  1. Select the “Help” menu > “Install New Software...”.

> 2. In the “Work with” text box, enter:

[http://dl.google.com/eclipse/plugin/3.5](http://dl.google.com/eclipse/plugin/3.5)

Click the “Add...” button. In the dialog that shows, click “OK” (keep the name blank, it will be retrieved from the date site.)

&nbsp;&nbsp;3. Click the triangle next to "Plugin" and "SDKs". Check the boxes next to "Google Plugin for Eclipse 3.5" and "Google App Engine Java SDK". You can also select the "Google Web Toolkit SDK" if you’d like to use [Google Web Toolkit](http://code.google.com/webtoolkit/) with your apps. Click the “Next” button. Follow the prompts to accept the terms of service and install the plugin.

> 4. When the installation is complete, Eclipse prompts you to restart. Click “Yes”. Eclipse restarts. The plugin is installed.

To install the plugin, using “Eclipse 3.4 (Ganymede)”:

  1. Select the “Help” menu > “Software Updates...” The "Software Updates and Add-ons" window opens.

> 2. Select the “Available Software” tab. Click the “Add Site...” button. The "Add Site" window opens. For "Location," enter the install location for the Eclipse 3.4 version of the plugin:

http://dl.google.com/eclipse/plugin/3.4

Click “OK”. The dialog closes, and the new location is added to the list of available software.

> 3. Click the triangle next to the new install location, then click the triangle next to "Google" to display installation options. Check the boxes next to "Google Plugin for Eclipse 3.4" and "Google App Engine Java SDK". You can also select the "Google Web Toolkit SDK" if you’d like to use [Google Web Toolkit](http://code.google.com/webtoolkit/) with your apps. Click the “Install...” button. Follow the prompts to accept the terms of service and install the plugin.

<u>Note</u>: The plugin install process may take several minutes to complete as Eclipse updates dependencies. See the FAQ for more information.

> 4. When the installation is complete, Eclipse prompts you to restart. Click “Yes”. Eclipse restarts. The plugin is installed.

The process for installing the plugin for “Eclipse 3.3 (Europa)” is similar, but with different prompts and a different install location. The location for the Google Plugin for Eclipse 3.3 is as follows:

-  http://dl.google.com/eclipse/plugin/3.3
See the Eclipse documentation for more information about Software Update.

<_Reference Google code_>

## Update to get latest version (if any): ##
-  Start Eclipse.
-  In Menu, choose Help – Software Updates
-  Click ’Add Site’, add http://dl.google.com/eclipse/plugin/3.4
-  Then add text to filter: "Google". Check into checkbox:
  1. Google Plugin for  Eclipse 3.4

> 2. Google App Engine Java SDK 1.2.5

> 3. Google Web Toolkit SDK 1.7.1

-  Click ’Install’ button. Wait a minutes then restart Eclipse
# Discovery #
## Import and compile project: ##
### Import source code: ###
#### Way 1: Get from subversion: ####
-  In Menu Toolbar, choose File – New – Other
-  In SVN folder choose "Checkout Projects from SVN"<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image002.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image002.jpg)<br />
<font color='#0070C0'>Image 1 – Checkout Projects from SVN</font>

-  Click Next. Choose ’Create a new repository location’
-  Input into Url text field with content: http://bkitpoma.googlecode.com/svn/trunk/ <br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image004.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image004.jpg)<br />
<font color='#0070C0'>Image 2 – Insert Url to check out</font>

-  Then click ’Next’ and after that click ’Finish’
-  Wait to download source code from SVN.

#### Way 2: ####
-  Go to [download BkitPoma.zip http://code.google.com/p/bkitpoma/downloads/list download BkitPoma.zip](http://code.google.com/p/bkitpoma/downloads/list) and then extract it.
-  Start Eclipse, in Menu toolbar, choose File – Import…
-  In General folder choose ’Existing Projects into workspace’, click Next
<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image006.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image006.jpg)
<font color='#0070C0'><br />Image 3 – Insert from existing project</font><br />

-  Browse to source code Folder (BkitPoma)
-  After you import successfully, the screen as following:<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image008.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image008.jpg)
<font color='#0070C0'><br />Image 4 – Import successfully</font><br />

### Configure for Build Path Project: ###
<u>Note:</u> Only do this step when have error like this:
<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image010.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image010.jpg)
<font color='#0070C0'><br />Image 5 – Error because build path incorrectly</font><br />

-  Right click in BkitPoma project, choose Build Path – Configure Build Path …<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image012.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image012.jpg)
<font color='#0070C0'><br />Image 6 – Configure Build path</font><br />

-  Correct build path have error with default setup
### Compile ###
#### Compile in Web mode: ####
-  Click Run in Toolbar:<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image014.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image014.jpg)
<font color='#0070C0'><br />Image 7 - Compile</font><br />

-  Choose Run with mode Web Application and choose home.jsp as start point.
-  If run successfully project in Hosted mode as follow:<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image016.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image016.jpg)
<font color='#0070C0'><br />Image 8 – Project BkitPoma in Hosted Mode</font><br />

#### Run in Web mode ####
-  Click into Compile/Browse button in Hosted Browser<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image018.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image018.jpg)
<font color='#0070C0'><br />Image 9 – Choose ’Compile/Browse’ to compile into Web mode</font><br />

-  Wait a minutes to compile, after that your default browser will start as follow:<br />
![http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image020.jpg](http://12a1nhc.com/duyhoai/RKH/Projects/Poma/Tutorials/Resources/Images/Guide%20configuration%20to%20compile%20and%20deploy%20BkitPoma/image020.jpg)
<font color='#0070C0'><br />Image 10 – Project BkitPoma in Web mode</font><br />

# Deploy project in Google App Engine: #
For more detail, see: <br />[http://code.google.com/appengine/articles/domains.html](http://code.google.com/appengine/articles/domains.html) <br />