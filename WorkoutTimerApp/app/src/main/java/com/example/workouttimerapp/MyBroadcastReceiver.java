package com.example.workouttimerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the broadcast event
        String action = intent.getAction();
        if (action.equals("my.custom.action")) {
            // Handle the custom action
            // For example, update the UI, start/stop the timer, etc.
        } else if (action.equals("my.custom.action.stop")) {
            // Handle the "stop" action
            // For example, stop the timer or reset the UI
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("my.custom.action");
        filter.addAction("my.custom.action.stop");
        return filter;
    }
}