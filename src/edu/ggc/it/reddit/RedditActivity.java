package edu.ggc.it.reddit;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import edu.ggc.it.R;

/**
 * author: JJ Lindsay
 * version: 1.0
 * Course: ITEC 4550 Spring 2015
 * Written: 5/6/2015
 *
 * Purpose: Allows users to access GGC reddit and it provides the ability to zoom in as well.
 */
public class RedditActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit);

        WebView webView = (WebView) findViewById(R.id.webView3);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        webView.setInitialScale(67);
        webView.loadUrl("http://www.reddit.com/r/ggc");

        webView.setWebViewClient(new WebViewClient());
    }
}