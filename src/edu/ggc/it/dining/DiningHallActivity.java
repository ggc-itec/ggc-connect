package edu.ggc.it.dining;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import edu.ggc.it.R;

/**
 * author: JJ Lindsay
 * version: 1.0
 * Course: ITEC 3870 Spring 2015
 * Written: 4/3/2015
 *
 * Purpose: Allows the user to view what is on today's menu, all within the app.
 */
public class DiningHallActivity extends Activity
{
    private WebView webView;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_eat);

        webView = (WebView) findViewById(R.id.webView2);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setInitialScale(67);
        webView.loadUrl("http://ggc.campusdish.com/Commerce/Catalog/Menus.aspx?LocationId=7123");

        webView.setWebViewClient(new WebViewClient());
    }
}