package net.edarling.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.phonegap.DroidGap;

public class NPNActivity extends DroidGap
{

    public void onCreate(Bundle savedInstanceState)
    {
        NPNPlugin.getInstance();

        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }


}
