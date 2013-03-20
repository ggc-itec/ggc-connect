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


public class DirectorySearchWebView extends Activity {

	WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String message = intent.getStringExtra(DirectoryActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_directory_search_web_view);
		mWebView = (WebView) findViewById(R.id.webView1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
            	//Filter telephone number clicks to open telephone
            	if (url.substring(0, 3).equals("tel")) {
            		Intent intent = new Intent(Intent.ACTION_DIAL);
            		intent.setData(Uri.parse(url));
            		startActivity(intent);
            	    return true;
                }
            	
            	//Filter email address clicks to open email client
            	else if (url.substring(0,6).equals("mailto")) {
            		Intent intent = new Intent(Intent.ACTION_SEND);
            		intent.setType("message/rfc822");
            		//adds email address to intent
            		intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { url.substring(7) });
            		//adds tag at end of email
            		intent.putExtra(Intent.EXTRA_TEXT, "\n" + "\n" + "\n" + "Sent from GGC Connect");
            		//lets user choose their email client
            		startActivity(Intent.createChooser(intent, "Send Email Via"));
            		return true;
            	}
            	
            	else {
                //else handles all other clicks to load in webview
            	view.loadUrl(url);
                return true; 
            	}
           }
            
            
        });
        mWebView.loadUrl(message);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        else {
        	finish();
        	return false;
        }
    }
}
