package edu.ggc.it.dining;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import edu.ggc.it.R;

/**
 * author: JJ Lindsay
 * version: 1.0
 * Course: ITEC 3870 Spring 2015
 * Written: 4/3/2015
 *
 * This class represents a ...
 *
 * Purpose: Allows the manipulation of a ...
 */
public class DiningHallActivity extends Activity
{
    private WebView webView;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_eat);

        webView = (WebView) findViewById(R.id.webView2);
        // experiment with this!!
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://ggc.campusdish.com/Commerce/Catalog/Menus.aspx?LocationId=7123");

        // experiment with this too!
        //you can create and display your own web document
        //String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
        // hmmn, what are "text/html"and "UTF-8" ??
        //webView.loadData(customHtml, "text/html", "UTF-8");
    }
}
