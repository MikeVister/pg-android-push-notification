Native Phonegap Notification Plugin
=====================
If you ever wanted to to send and receive push notifications on your own without a proxy like Urban Airship,
this Android plugin might be suitable for you.

The target audience for this plugin are Android developers with some experience in coding and debugging.
If you are new to Phonegap and Android, pls. refer to the Urban Airship plugin (https://github.com/tmaus/callback-android-pushNotification).


Releases:
=========

0.5
---
   No idea why my releases start with 0.5.
   It mainly indicates that it might not yet be stable.





Prerequisites:
=============

1. You need to be familiar with the Phonegap framework (tested  against 1.4.1).

2. You need to have a valid Google account.

3. You should be familiar with Android development.

4. Android mobile device


Preparations
============

Google
-------

You need a Google account to send a receive notifications.
We have created a dedicated account for C2DM that is solely used for mobile activities.

Login to your Google account and sign up for Google's C2DM (http://code.google.com/intl/de-DE/android/c2dm/signup.html).
C2DM is used to deliver notifications to your device.

Hints:
Google asks for the package name
This plugin uses "net.edarling.mobile" as base package as well as identifier for the app.
I wrote this plugin as part of our mobile development and thus used our package naming.
Pls. adjust the package structure to your needs.
Only if the package set at C2DM registration matches your AndroidManifest package name, any message can be delivered and received.

Whenever your service sends a notification to a user, it needs to authenticate against Google.
As an additional layer of security, Google provides the following rules:
1. To bootstrap the complete process, the user has to uniquely fetch an auth-token by processing a "ClientLogin" for the given account.

curl https://www.google.de/accounts/ClientLogin -d Email=_YOUR_USER_@googlemail.com -d Passwd=###### -d accountType=GOOGLE -d source=_PACKAGE_NAME -d service=ac2dm

2. The response covers the parameter "auth". This auth-token must be used for every consequently request until it expires.

3. A send notification request to google could contain a new "auth-token" as part of the response header (Update-Client-Auth). In this case only use the new "auth-token" for consequently calls to Google.






The Plugin
==========

I have added an "index.html" file to the plugin to ease some pain.
Most of the  magic happens inside the "NPNC2DMReceiver" class.
It receives notifications and distributes them to the user via notification bar or direct javascript callback

The "NPNPlugin" class covers a commonly known layout.
We have a central method (execute) that is called by every javascript call.
It delegates the requested functions to private methods.

Nothing really new here if you have ever coded your own plugin for Phonegap.


Sending a notification
======================

. . . . in progress . . .



Links:
=======
Phonegap : http://www.phonegap.com
C2DM Homepage: http://code.google.com/intl/de-DE/android/c2dm/
C2DM Java Client: https://github.com/notnoop/java-c2dm

Contact:
========
Feel free to contact me in case of any problems
eMail: maus[@]pirack.com


