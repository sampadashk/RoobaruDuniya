package com.samiapps.kv.roobaruduniya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by KV on 20/6/17.
 */

public class PrivacyPolicy extends AppCompatActivity {
    private TextView tv;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        tv=(TextView)findViewById(R.id.about_us);
        tv.setText(R.string.privacy);
    }
}
