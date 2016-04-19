package com.raider.rssdgea.rssBuilding;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.raider.rssdgea.dataTemplates.RssItem;

import org.xml.sax.Attributes;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raider on 14/04/16.
 */
public class RssParser {
    private URL rssUrl;
    private RssItem rssItem;

    public RssParser() {
        try {
            this.rssUrl = new URL("http://dgea.es/inicio/index.php?format=feed&type=rss");
        } catch (MalformedURLException murle) {
            throw  new RuntimeException(murle);
        }
    }

    public List<RssItem> parse() {

        final List<RssItem> items = new ArrayList<>();

        RootElement root =  new RootElement("rss");
        Element channel = root.getChild("channel");
        final Element item = channel.getChild("item");


        item.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                rssItem = new RssItem();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            @Override
            public void end() {
                items.add(rssItem);
            }
        });

        item.getChild("title").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setTitle(s);
            }
        });

        item.getChild("link").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setUrl(s);
            }
        });

        item.getChild("guid").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setGuid(s);
            }
        });

        item.getChild("description").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setDescription(s);
            }
        });

        item.getChild("author").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setAuthor(s);
            }
        });

        item.getChild("category").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setFirstCategory(s);
            }
        });

        item.getChild("pubDate").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                rssItem.setPubDate(s);
            }
        });

        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8,root.getContentHandler());
            if (this.getInputStream() != null) this.getInputStream().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    private InputStream getInputStream() {

        try {
            return rssUrl.openConnection().getInputStream();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
