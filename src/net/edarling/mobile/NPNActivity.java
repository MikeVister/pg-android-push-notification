package net.edarling.mobile;

import android.app.Activity;
import android.os.Bundle;
import com.phonegap.DroidGap;

public class NPNActivity extends DroidGap
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        NPNPlugin.getInstance();

        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
