package com.sharifdev.torobche;

import android.app.Application;

import com.parse.Parse;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // enable parse local data store
        // Parse.enableLocalDatastore(this);
        // initialize backEnd Connection
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("KzyaJzgW3mW7sxJX") // should correspond to APP_ID env variable
                .server("https://195.248.241.235:1337/parse/").build());
    }
}
