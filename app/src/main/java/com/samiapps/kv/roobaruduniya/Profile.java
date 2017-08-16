package com.samiapps.kv.roobaruduniya;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by KV on 15/8/17.
 */

public class Profile extends AppCompatActivity {
    TabLayout tabLayout;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Published"));


    }
}