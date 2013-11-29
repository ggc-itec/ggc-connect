package edu.ggc.it.calendar;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import edu.ggc.it.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.webkit.WebSettings;
import android.webkit.WebView;


/*
 * Shows GGC's academic calendar on a WebView
 * 
 * This is more of an example of using Jsoup to download the contents of a URL and 
 * display it with some embellishment :)
 * 
 * 
 * TODO: please somebody redesign the icon for this activity, IMOH, it could look better
 * 
 */
public class CalendarActivity extends Activity
{

    public static final String GGC_ACADEMIC_CALENDAR_ADDRESS = "http://www.ggc.edu/academics/calendar/";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        webView = (WebView) findViewById(R.id.calendar_webview);
        webView.getSettings().setJavaScriptEnabled(false);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        webView.setBackgroundColor(Color.parseColor("#4C7801")); // GGC color!
        DownloadCalendarTask task = new DownloadCalendarTask(CalendarActivity.this);
        task.execute();
    }

    private class DownloadCalendarTask extends AsyncTask<Void, Void, String> 
    {    
 
        private ProgressDialog dialog;

        public DownloadCalendarTask(Context context) 
        {
            dialog = new ProgressDialog(context);
        }
        
        protected void onPreExecute() 
        {
            dialog.setMessage("Downloading...");
            dialog.show();
        };
        
        @Override
        protected String doInBackground(Void... params) 
        {
            Document doc = null;
            try
            {
                doc = Jsoup.connect(GGC_ACADEMIC_CALENDAR_ADDRESS).get();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Elements newsHeadlines = doc.select(".one_twoCol"); // class one_twoCol is a CSS selector for the calendar table (this can change!)
            String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
                    "<style> table, td, th { border-collapse:collapse; border:1px solid black; } </style></head><body>"; //apply css table style
            html = html + newsHeadlines.toString();
            html = html + "</body></html>";
            return html;
        }

        @Override
        protected void onPostExecute(String result) 
        {
            webView.loadData(result,"text/html; charset=utf-8", "utf-8");
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

    }
 
}
