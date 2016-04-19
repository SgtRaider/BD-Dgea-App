package com.raider.rssdgea.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Raider on 19/04/16.
 */
public class NotificationService extends IntentService {

    public static final String PROGRESS = "com.raider.intent.action.PROGRESS";
    public static final String END = "com.raider.intent.action.END";

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("hola");
        return super.onBind(intent);
    }
}
