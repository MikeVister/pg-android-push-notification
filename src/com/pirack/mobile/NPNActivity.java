package com.pirack.mobile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.apache.cordova.DroidGap;

/**
 * Main activity.
 * Opens the WebView for the Phonegap framework
 * Is called as well as PendingIntent by clicking on message in notification tab.
 *
 *
 * User: tmaus (maus@pirack.com)
 */
public class NPNActivity extends DroidGap{

    private final static String TAG = "NPNActivity";

    /**
     *
     * @param b
     */
    public void onCreate(Bundle b){
        super.onCreate(b);
        super.loadUrl("file:///android_asset/www/index.html");

        if(getIntent().getExtras()!=null){
            Log.d(TAG,"intent has extras; app is booting");
            processNotificationBoot(getIntent().getExtras());
        }

        if(b!=null){
            Log.d(TAG,"booting via notification");
            processNotificationBoot(b);
        }
    }

    /**
     *
     * @param intent
     */
    public void onNewIntent(Intent intent){
        if(intent.getExtras()!=null){
            processNotificationBoot(intent.getExtras());
        }
    }

    /**
     *  Checks for NOTIFICATION_BOOT flag and calls plugin accordingly.
     *
     * @param b
     */
    private void processNotificationBoot(Bundle b){
        // might be useful for debugging
        for(String key: b.keySet()){
            Log.d(TAG,"key:"+key+", value:"+b.get(key));
        }

        // on app boot or resume, delay delivery of notification to allow pg to fully bootstrap
        if(b.containsKey(NPNReceiver.NOTIFICATION_BOOT)){
            Intent i = new Intent(this,NPNDelayedDeliveryIntent.class);
            i.putExtras(b);
            startService(i);
        }
    }

    /**
     *
     */
    protected void onPause() {
        super.onPause();
        NPNPlugin.getInstance().setIsRunning(false);
    }

    /**
     *
     */
    protected void onResume() {
        super.onResume();
        NPNPlugin.getInstance().setIsRunning(true);
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();
        NPNPlugin.getInstance().setIsRunning(false);
    }
}
