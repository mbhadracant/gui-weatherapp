Installation
----------------------------------------------------------------------------------------------------------------------------
-> The Android app can be installed in two ways:
    - APK installation
    - Emulator Installation

APK Installation
---------------------
- We have attached an APK for this app which can be installed in an Android mobile phone. 
In order to install the app, it must be transferred to the phone. There are many ways in which 
this can be done. One way is to upload this file to a cloud service such as Google Drive, Dropbox or 
by sending it to your email and accessing it from your mobile mail app. Another way to store the APK 
file to a mobile phone by transferring it from a computer. Connect the mobile to a computer using USB
cable and transfer it.

- Once the APK file has been transferred, make sure the Debug mode is enabled. 
This can be done following the steps below. It is important that debugging mode is enabled, 
otherwise you will not be able install the app.

- Settings -> Developer options -> USB debugging 
Note: this may vary based on the mobile phone.

Emulator Installation
---------------------------
Download the installer from http://developer.android.com/sdk/index.html

1) Install it to a location (like: C:\Program Files\Android)

2) Launch “SDK Manager.exe”

3) It will launch a “Android SDK and AVD Manager”. By default “Installed packages” will be highlighted. 
   On the pop up “Choose Packages to Install”, there will be some packages selected already. 
   Go ahead with the “Install”.

4) Once it is done, select “Virtual Devices” to create one for you. Click on “New” and enter a name. 
   Set the resolution to 320*480.

5) Once done select the Virtual device you created and click on Start. You can find the “Start” button 
   on the right panel in the above shown image. Clicking “Start” will launch another pop-up, just say 
   “Launch” and proceed.

6) Now, manually copy and paste the app-debug.apk  to the destination folder 
   "E:\android-sdk-windows\tools\".

7) Go to Start>>run>>cmd (open a windows command prompt) and type the following commands. 
   cd e:\android-sdk-windows\tools\ 

8) adb install app-debug.apk
Note: To switch orientation on Windows ctrl+F11, On Mac Fn+ctrl+F11 for switching orientation


Pre-condition to run the app:
- Make sure the mobile phone is connected to the internet
- At least 50MB of memory is required

Library used
----------------
We have not used any library in our app as the application is created for Android platform. 
It doesn’t require any library. We have however used MetOffice to retrieve weather information. 
It is not a library but its an external source. We have parsed MetOffice HTML document to 
retrieve weather information.

---------------------------------------------------------------------------
Team Members: Kar-Sing Zak Carr, Kowrishankar Kaneshamoorthy, Jackil Rajnicant, Mayur Bhadracant
Group-Name: TeamGG
Group-No: 29
