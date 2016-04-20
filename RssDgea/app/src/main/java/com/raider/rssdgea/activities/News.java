package com.raider.rssdgea.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.raider.rssdgea.R;
import com.raider.rssdgea.adapters.RssAdapter;
import com.raider.rssdgea.dataTemplates.RssItem;
import com.raider.rssdgea.database.DbInteraction;
import com.raider.rssdgea.rssBuilding.RssParser;
import com.raider.rssdgea.services.NotificationService;
import com.raider.rssdgea.services.NotificationService.ServiceBinder;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {

    private ListView list;
    private Refresh asynctask;
    private List<RssItem> items;
    private List<RssItem> newEntries;
    private RssAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private DbInteraction db;
    private RssParser saxparser;
    private Intent msgIntent;
    private ServiceBinder binder;
    private Toast refreshMsg;
    private NotificationService notificationService;

    private boolean isServiceBinded = false;
    private boolean isPaused = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (ServiceBinder) iBinder;
            notificationService = binder.getService();
            notificationService.setNews(News.this);
            isServiceBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBinded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        items = new ArrayList<>();
        db = new DbInteraction(this, "dbRssFeed",null,1);


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        asynctask = new Refresh();
                        asynctask.execute();
                    }
                }
        );

        list = ((ListView) findViewById(R.id.list));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedUrl = ((RssItem) adapterView.getItemAtPosition(i)).getUrl();
                Intent intent = new Intent(News.this, UrlViewer.class);
                Bundle b = new Bundle();
                b.putString("url", selectedUrl);
                intent.putExtras(b);

                startActivity(intent);
            }
        });

        adapter = new RssAdapter(this, items);
        list.setAdapter(adapter);

        asynctask = new Refresh();
        asynctask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;

        if (!isServiceBinded) {
            msgIntent = new Intent(News.this, NotificationService.class);
            startService(msgIntent);
            bindService(msgIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationService != null && notificationService.getNotificationManager() != null) {
            notificationService.deleteNotifications();
        }

        asynctask = new Refresh();
        asynctask.execute();

        isPaused = false;
    }

    @Override
    protected void onStop() {
        stopService(msgIntent);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class Refresh extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saxparser = new RssParser();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(String... strings) {
            items = saxparser.parse();
            db.saveItems(items);
            return null;
        }

        @Override
        protected void onPostExecute(Void aBoolean) {
            super.onPostExecute(aBoolean);
            items.clear();
            adapter.clear();
            adapter.addAll(db.getList());
            adapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            refreshMsg = Toast.makeText(getApplicationContext(), R.string.refresh_data, Toast.LENGTH_SHORT);
            refreshMsg.show();
        }
    }

    private class CheckNewEntries extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saxparser = new RssParser();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!newEntries.isEmpty()) notificationService.callNotification(newEntries);
            items.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            items = saxparser.parse();
            newEntries = db.checkNewEntries(items);
            return null;
        }
    }

    public void checkEntries() {
        CheckNewEntries check = new CheckNewEntries();
        check.execute();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void unbindService() {
        notificationService.unbindService(serviceConnection);
    }

    public void setIsServiceBinded(boolean isBinded) {
        this.isServiceBinded = isBinded;
    }

    public List<RssItem> getNewEntries() {
        return newEntries;
    }
}
