package net.edarling.mobile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Responsible for processing incoming notifications sent to our package signature.
 *
 * User: tmaus (maus@pirack.com)
 */
public class NPNC2DMReceiver extends BroadcastReceiver {

    private static String KEY = "c2dmPref";
    private static String REGISTRATION_KEY = "registrationKey";
    private final String TAG = "NPNC2DMReceiver";
    public static  final String NOTIFICATION_BOOT = "notificationBoot";
    
    
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive with action:" + intent.getAction()+"!");

        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {

            // the message we want to display
            CharSequence tickerText;

            try{
                tickerText = intent.getExtras().get("message").toString();
            }catch(NullPointerException npe){
                tickerText = "Dude, no key=message has been provided. ";
            }

            // If app is in foreground, push message back to JS layer
            if(NPNPlugin.getInstance().isRunning()){
                NPNPlugin.getInstance().deliverNotification(intent.getExtras());
            }else{
                // create status-bar notification
                NotificationManager nm = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                int icon = R.drawable.icon_small;
                Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
                Intent mainIntent = new Intent(Intent.ACTION_MAIN);
                mainIntent.setClass(context, NPNActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mainIntent.putExtras(intent.getExtras());
                mainIntent.putExtra(NOTIFICATION_BOOT,true);

                // Intent that is called once user clicks on notification
                PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setLatestEventInfo(context, "eDarling", tickerText, contentIntent);
                nm.notify(1, notification);

                // display a toast to the user to inform him on the incoming notification
                LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
                View layout = inflater.inflate(R.layout.notification, null);
                ImageView image = (ImageView) layout.findViewById(R.id.image);
                image.setImageResource(R.drawable.icon_small);
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(tickerText);
                Toast toast = new Toast(context.getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }


        }else {
        
            Log.w(TAG,"action: " + intent.getAction() + " has no match !!!");
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
