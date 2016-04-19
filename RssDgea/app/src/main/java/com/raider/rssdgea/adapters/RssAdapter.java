package com.raider.rssdgea.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.raider.rssdgea.R;
import com.raider.rssdgea.dataTemplates.RssItem;

import java.util.List;

/**
 * Created by Raider on 14/04/16.
 */
public class RssAdapter extends ArrayAdapter<RssItem> {

    Activity context;
    List<RssItem> data;

    public RssAdapter (Activity context, List<RssItem> data) {
        super(context, R.layout.rssitem_news, data);
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.rssitem_news, parent, false);

        TextView lblTitle = (TextView) item.findViewById(R.id.lblTitle);
        lblTitle.setText(data.get(position).getTitle());

        TextView lblCategory = (TextView) item.findViewById(R.id.lblCategory);
        lblCategory.setText(data.get(position).getFirstCategory());

        return item;
    }
}
