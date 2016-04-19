package com.raider.rssdgea.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.raider.rssdgea.R;

public class UrlViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_viewer);

        WebView webView = (WebView) this.findViewById(R.id.urlViewer);
        webView.loadUrl(((Bundle) this.getIntent().getExtras()).getString("url"));
    }
}
