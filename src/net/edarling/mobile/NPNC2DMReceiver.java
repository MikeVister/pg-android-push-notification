package net.edarling.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: tmaus
 * Date: 27.03.12
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class NPNC2DMReceiver extends BroadcastReceiver {

    private static String KEY = "c2dmPref";
    private static String REGISTRATION_KEY = "registrationKey";
    private final String TAG = "NPNC2DMReceiver";

    
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive with action:" + intent.getAction()+"!");

        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            NPNPlugin.getInstance().deliverNotification(intent.getExtras());
        }else {
        
        Log.w(TAG,"action: " + intent.getAction() + " does not match");
        }
    }

    
    private void handleRegistration(Context context, Intent intent) {
        Log.d(TAG,"handling registration");
        
        String registration = intent.getStringExtra("registration_id");
        if (intent.getStringExtra("error") != null) {
            // Registration failed, should try again later.
            Log.d(TAG, "registration failed");
            String error = intent.getStringExtra("error");
            if(error == "SERVICE_NOT_AVAILABLE"){
                Log.d(TAG, "SERVICE_NOT_AVAILABLE");
            }else if(error == "ACCOUNT_MISSING"){
                Log.d(TAG, "ACCOUNT_MISSING");
            }else if(error == "AUTHENTICATION_FAILED"){
                Log.d(TAG, "AUTHENTICATION_FAILED");
            }else if(error == "TOO_MANY_REGISTRATIONS"){
                Log.d(TAG, "TOO_MANY_REGISTRATIONS");
            }else if(error == "INVALID_SENDER"){
                Log.d(TAG, "INVALID_SENDER");
            }else if(error == "PHONE_REGISTRATION_ERROR"){
                Log.d(TAG, "PHONE_REGISTRATION_ERROR");
                NPNPlugin.getInstance().deliverActivationResult(error,false);
            }
        } else if (intent.getStringExtra("unregistered") != null) {
            Log.d(TAG, "unregistered device, no need for a msg");
        } else if (registration != null) {
            Log.d(TAG, "fetched registrationId:" + registration);
            SharedPreferences.Editor editor =
                    context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            editor.putString(REGISTRATION_KEY, registration);
            editor.commit();
            NPNPlugin.getInstance().deliverActivationResult(registration,true);
        }
        
    }
}
