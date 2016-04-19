package com.raider.rssdgea.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Raider on 19/04/16.
 */
public class DataReciver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(NotificationService.PROGRESS)) {

            System.out.println("Funciona");
        } else {

            if (intent.getAction().equals(NotificationService.END)) {

            }
        }
    }
}
