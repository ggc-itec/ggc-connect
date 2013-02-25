package edu.ggc.it;


import edu.ggc.it.directory.DirectoryActivity;
import android.os.Bundle;
import android.webkit.WebView;
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
        mWebView.loadUrl(message);
	}


}
