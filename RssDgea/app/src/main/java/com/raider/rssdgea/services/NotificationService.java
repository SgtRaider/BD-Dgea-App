package com.raider.rssdgea.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.raider.rssdgea.R;
import com.raider.rssdgea.activities.News;
import com.raider.rssdgea.dataTemplates.RssItem;

import java.util.List;

/**
 * Created by Raider on 19/04/16.
 */
public class NotificationService extends IntentService {

    private News news;
    private NotificationManager notificationManager;
    private IBinder serviceBinder = new ServiceBinder();
    private NotificationCompat.Builder notificationBuilder;
    private StringBuilder sb;
    private final int NOTIF_NOTICE_ID = 001;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (true) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (news.isPaused()) {
                try {
                    Thread.sleep(25000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                news.checkEntries();
            } else {

                if (!news.isPaused()) {

                    try {
                        Thread.sleep(55000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Activo");
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        news.setIsServiceBinded(false);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        news.unbindService();
        news.setIsServiceBinded(false);
        super.onDestroy();
    }

    public void setNews(News news) {
        this.news = news;
    }

    public class ServiceBinder extends android.os.Binder {

        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    private void notifyNewEntry(List<RssItem> items) {

        buildNotificationText(items);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle(getResources().getString(R.string.dgea_needs_you))
                .setContentText(sb.toString())
                .setTicker(getResources().getString(R.string.dgea_needs_you))
                .setContentInfo(String.valueOf(news.getNewEntries().size()));

        Intent notIntent = new Intent(this, News.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
    }

    public void callNotification(List<RssItem> items) {

        notifyNewEntry(items);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIF_NOTICE_ID, notificationBuilder.build());
    }

    public void deleteNotifications() {

        notificationManager.cancelAll();
    }

    private void buildNotificationText(List<RssItem> list) {

        sb = new StringBuilder();
        for (RssItem r: list) {
            sb.append("-");
            sb.append(r.getTitle());
            sb.append("\n");
        }
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
