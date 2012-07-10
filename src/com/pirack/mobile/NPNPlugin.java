package com.pirack.mobile;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Implementation of a Phonegap plugin.
 * Responsible for the communication between JS layer and Java layer.
 *
 *
 * User: tmaus (maus@pirack.com)
 */
public class NPNPlugin extends Plugin {

    public static final String BOOTSTRAP = "bootstrap";
    
    public static final String ACTIVATE_NOTIFICATIONS  ="activatePush";
    public static final String DEACTIVATE_NOTIFICATIONS = "deactivatePush";

    // GCM project id
    public final static String PROJECT_ID = "42";

    private static final String TAG = "NPNPlugin";

    // we need the plugin as a singleton for more than one reason
    private static NPNPlugin instance = null;

    //
    private String activatePushCallbackId;

    // indicates, if the app is running in foreground or not
    private boolean isRunning = false;

    public NPNPlugin(){
        instance = this;
    }

    // In order to keep the binding to PG JS, we need the plugin as a singleton
    public static NPNPlugin getInstance(){
        if(instance == null){
            instance = new NPNPlugin();
        }
        return instance;
    }

    /**
     * Is called by every JS function.
     * Handles all kinds of "action's" and distributes to dedicated methods.
     *
     * @param action
     * @param jsonArray
     * @param callbackId
     * @return
     */
    public PluginResult execute(String action, JSONArray jsonArray, String callbackId) {
        Log.d(TAG,"process plugin for action:"+action);

        if(BOOTSTRAP.equals(action)){
            Log.i(TAG,"plugin properly bootstrapped");
            isRunning = true;
            return new PluginResult(PluginResult.Status.NO_RESULT);
        }

        if(ACTIVATE_NOTIFICATIONS.equals(action)){
            activatePush();
            activatePushCallbackId = callbackId;
            PluginResult ps = new PluginResult(PluginResult.Status.NO_RESULT);
            ps.setKeepCallback(true);
            return ps;
        }

        if(DEACTIVATE_NOTIFICATIONS.equals(action)){
            deactivatePush();
            return new PluginResult(PluginResult.Status.OK);
        }

        return new PluginResult(PluginResult.Status.INVALID_ACTION);
    }

    /**
     *
     * @param msg
     * @param status
     */
    public void deliverActivationResult(String msg, boolean status){
        Log.d(TAG,"send registrationId back to JS");
        PluginResult result = new PluginResult(status ? PluginResult.Status.OK : PluginResult.Status.ERROR, msg);
        result.setKeepCallback(false);
        this.success(result, this.activatePushCallbackId);
    }
    
    /**
     *
     * @param b
     */
    public void deliverNotification(Bundle b){
        JSONObject jo = new JSONObject();
        for(String key:b.keySet()){
            try {
                jo.put(key,b.get(key));
            } catch (JSONException e) {
                Log.e(TAG,"cannot ");
            }
        }
        Log.d(TAG,"notification message on delivery: " + jo.toString());
        this.sendJavascript(String.format("window.plugins.NPNPlugin.notificationCallback(%s);", jo.toString()));
    }

    /**
     *
     * @return
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     *
     * @param b
     */
    public void setIsRunning(boolean b){
        isRunning = b;
    }

    /**
     *
     */
    private void activatePush(){
        Log.d(TAG,"activate push");
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(ctx.getApplicationContext(), 0, new Intent(), 0));
        registrationIntent.putExtra("sender", PROJECT_ID);
        ctx.getApplicationContext().startService(registrationIntent);
    }

    /**
     *
     */
    private void deactivatePush(){
        Log.d(TAG,"deactivate push");
        Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        unregIntent.putExtra("app", PendingIntent.getBroadcast(ctx.getApplicationContext(), 0, new Intent(), 0));
        ctx.getApplicationContext().startService(unregIntent);
    }

}
