package edu.ggc.it.directory;

import edu.ggc.it.R;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class DirectorySearchWebView extends Activity
{
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String message = intent.getStringExtra(DirectoryActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_directory_search_web_view);
		webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
            	if (url.substring(0, 3).equals("tel")) {
            		Intent intent = new Intent(Intent.ACTION_DIAL);
            		intent.setData(Uri.parse(url));
            		startActivity(intent);
            	    return true;
                } else if (url.substring(0,6).equals("mailto")) {
            		Intent intent = new Intent(Intent.ACTION_SEND);
            		intent.setType("message/rfc822");
            		intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { url.substring(7) });
            		intent.putExtra(Intent.EXTRA_TEXT, "\n" + "\n" + "\n" + "Sent from GGC Connect");
            		startActivity(Intent.createChooser(intent, "Send Email Via"));
            		return true;
            	} else {
            	    view.loadUrl(url);
                    return true;
            	}
           }
            
            
        });
        webView.loadUrl(message);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
        	finish();
        	return false;
        }
    }
}