package edu.ggc.it;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

public class Credits extends Activity {

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// will throw exception if you do not call this
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits);

		webView = (WebView) findViewById(R.id.creditsWebView);
		webView.getSettings().setJavaScriptEnabled(false);
		webView.setBackgroundColor(Color.BLACK);
		webView.loadData("<font color='green'><center> <p>Leo Hernandez" +
				"<p>Andrew Lynch" +
				"<p>Jared Marquez" +
				"<p>Jesse Perkins" +
				"<p>Thai Pham" +
				"<p>Rajesh Ramsaroop" +
				"<p>Jacob Smallwood" +
				"<p>Felegh Solomon" +
				"</center></font>",
				"text/html",
				"UTF-8");
	}

}
