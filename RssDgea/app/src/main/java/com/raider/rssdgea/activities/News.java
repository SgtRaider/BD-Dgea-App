package com.raider.rssdgea.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.raider.rssdgea.R;
import com.raider.rssdgea.adapters.RssAdapter;
import com.raider.rssdgea.dataTemplates.RssItem;
import com.raider.rssdgea.database.DbInteraction;
import com.raider.rssdgea.rssBuilding.RssParser;
import com.raider.rssdgea.services.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {

    private ListView list;
    private Refresh asynctask;
    private List<RssItem> items;
    private RssAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private DbInteraction db;
    private RssParser saxparser;
    private Intent msgIntent;
    public static Boolean onPause = false;

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
        Intent msgIntent = new Intent(News.this, NotificationService.class);
        startService(msgIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent msgIntent = new Intent(News.this, NotificationService.class);
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
        }
    }
}
