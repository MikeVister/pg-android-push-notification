Native Phonegap (Cordova) Notification Plugin
=====================
If you ever wanted to to send and receive push notifications on your own without a proxy like Urban Airship,
this Android plugin might be suitable for you.

The target audience for this plugin are Android developers with some experience in coding and debugging.
If you are new to Phonegap and Android, pls. refer to the Urban Airship plugin (https://github.com/tmaus/callback-android-pushNotification).


Releases:
=========

1.0
---
  Migration to Cordova 1.8.1
  Sample is compliant to Google GCM

0.5
---
   No idea why my releases start with 0.5.
   It mainly indicates that it might not yet be stable.


Prerequisites:
=============

1. You need to be familiar with the Phonegap (Cordova) framework (tested  against 1.8.1).

2. You need to have a valid Google account.

3. You should be familiar with Android development.

4. Android mobile device


Preparations
============

Google GCM
-------

Google Cloud Messaging, Google's Cloud Messaging framework allows third party applications to
send message to registered mobile devices.

Following the instructions at http://developer.android.com/guide/google/gcm/index.html to setup your GCM account






The Plugin
==========

After downloading and installing the sources, you have to add both, the "cordova-VERSION.jar" + "cordova-VERSION.js" to the approriate folders.

In the NPNPlugin.java, replace the SENDER_ID
public final static String SENDER_ID = "42";
by the project ID, GCM created for you.

Your PG app should start out-of-the-box.



Sending a notification
======================

We use "curl" to send a simple msg to our already registered device.

curl --header "Authorization: key=_API_KEY_" --data "registration_id=_REGISTRAION_ID_&data.msg=The answer is 42" https://android.googleapis.com/gcm/send




Links:
=======
http://developer.android.com/guide/google/gcm/index.html
http://developer.android.com/guide/google/gcm/gcm.html
http://phonegap.com/
http://code.google.com/p/chrome-rest-client/
http://incubator.apache.org/cordova/
https://github.com/tmaus/pg-android-push-notification
http://radar.oreilly.com/2011/10/phonegap-mobile-development.html



Contact:
========
Feel free to contact me in case of any problems
eMail: maus[@]pirack.com


