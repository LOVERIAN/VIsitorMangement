package com.homeone.visitormanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PortalActivity extends AppCompatActivity {
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);


        myWebView = findViewById(R.id.webView);
        myWebView.setWebViewClient(new WebPortal());

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("https://apartment.homeonetechnologies.in/");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v->{
            super.onBackPressed();
        });

    }

    private static class WebPortal extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}