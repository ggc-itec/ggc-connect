package edu.ggc.it.gym;

import edu.ggc.it.R;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import android.app.Activity;
import android.content.Intent;

public class MagazineActivity  extends Activity
{
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(edu.ggc.it.R.layout.activity_magazine);
		Intent intent =  new Intent(this, MagazineActivity.class);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(MagazineActivity.this, description,
						Toast.LENGTH_SHORT).show();
			}
			

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
			getMenuInflater().inflate(R.menu.activity_magazine, menu);
			return true;
	}
}

