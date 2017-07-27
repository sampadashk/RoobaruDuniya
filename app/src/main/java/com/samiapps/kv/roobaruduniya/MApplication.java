package com.samiapps.kv.roobaruduniya;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by KV on 24/7/17.
 */

public class MApplication extends Application {
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
