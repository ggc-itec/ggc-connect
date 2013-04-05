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


public class MagazineActivity  extends Activity{
	private WebView webView;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(edu.ggc.it.R.layout.activity_magazine);
		intent =  new Intent(this, MagazineActivity.class);
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
		public static void main(String[] args) 
		{
			String url = "http://readsh101.com/ggc.html";
			Activity act = new Activity();
			showWebSite(act, url);

		}
		private static void showWebSite(Activity activity, String url)
		{
			Intent webIntent = new Intent(Intent.ACTION_VIEW);
			webIntent.setData( Uri.parse(url));
			activity.startActivity(webIntent);
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_magazine, menu);
			return true;
		}
	}

