package net.edarling.mobile;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: tmaus  (maus@pirack.com)
 * Date: 27.03.12
 * Time: 10:06
 */
public class NPNPlugin extends Plugin{

    public static final String BOOTSTRAP = "bootstrap";
    
    public static final String ACTIVATE_NOTIFICATIONS  ="activatePush";
    public static final String DEACTIVATE_NOTIFICATIONS = "deactivatePush";
    
    public final static String SENDER_EMAIL = "edarling.mobile@googlemail.com";

    private static final String TAG = "NPNPlugin";

    private static NPNPlugin instance = null;

    private String activatePushCallbackId;
    
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
     */
    private void activatePush(){
        Log.d(TAG,"activate push");
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(ctx, 0, new Intent(), 0)); // boilerplate
        registrationIntent.putExtra("sender", SENDER_EMAIL);
        ctx.getApplicationContext().startService(registrationIntent);
    }

    /**
     *
     */
    private void deactivatePush(){
        Log.d(TAG,"deactivate push");
        Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        unregIntent.putExtra("app", PendingIntent.getBroadcast(ctx, 0, new Intent(), 0));
        ctx.getApplicationContext().startService(unregIntent);
    }

}
