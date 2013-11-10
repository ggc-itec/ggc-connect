package edu.ggc.it.myinfo;

import edu.ggc.it.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyInfoActivity extends Activity
{

	private WebView webView;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);
		// need to add internet user permission for this to work
		webView = (WebView) findViewById(R.id.myInfoWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl)
            {
				Toast.makeText(MyInfoActivity.this, description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageFinished(WebView view, String url)
            {
				if (progress != null && progress.isShowing())
					progress.dismiss();
			}

		});

		progress = ProgressDialog.show(MyInfoActivity.this, "Progress",
				"Loading...", true, true);
		webView.loadUrl("http://myinfo.ggc.edu");
	}
}
