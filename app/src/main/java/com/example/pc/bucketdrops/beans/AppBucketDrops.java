package com.example.pc.bucketdrops.beans;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;



public class AppBucketDrops extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
