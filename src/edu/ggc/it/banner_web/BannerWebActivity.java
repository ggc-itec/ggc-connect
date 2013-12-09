package edu.ggc.it.banner_web;

import edu.ggc.it.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/*
 * Activity that logs in and displays Banner in a WebView
 * 
 */
public class BannerWebActivity extends Activity
{
    public static final String BANNER_DOMAIN = "ggc.gabest.usg.edu";
    public static final String BANNER_LOGIN_URL = "https://ggc.gabest.usg.edu/pls/B400/twbkwbis.P_WWWLogin";
    public static final String BANNER_LOGIN_POST_URL = "https://ggc.gabest.usg.edu/pls/B400/twbkwbis.P_ValLogin";
    public static final String BANNER_MENU_URL = "https://ggc.gabest.usg.edu/pls/B400/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu";
    public static final String BANNER_LOGIN_NAME = "sid";
    public static final String BANNER_PASSWORD_NAME = "PIN";
    private static List<Cookie> cookies;
    private WebView webView;
    private String username;
    private String password;
    private String TAG = "BannerWebActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_web);
        Intent intent = getIntent();
        username = intent.getExtras().getString(BANNER_LOGIN_NAME);
        password = intent.getExtras().getString(BANNER_PASSWORD_NAME);
        webView = (WebView) findViewById(R.id.webview_banner);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
              return false;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName(HTTP.UTF_8);
        BannerLoginTask task = new BannerLoginTask(BannerWebActivity.this);
        task.execute();
    }
   
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
        {
            webView.goBack();
            return;
        }
        else
        {
            super.onBackPressed();
        }
        //remove cookies before leaving current activity
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        SystemClock.sleep(2000);
    }

    private class BannerLoginTask extends AsyncTask<Void, Void, Void> {
 
        private ProgressDialog dialog;

        public BannerLoginTask(Context context) {
            dialog = new ProgressDialog(context);
        }
        
        protected void onPreExecute() {
            dialog.setMessage("Logging in...");
            dialog.show();
        };
        
        @Override
        protected Void doInBackground(Void... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                
                HttpGet httpGet = new HttpGet(BANNER_LOGIN_URL);
                HttpResponse response1 = null;
                response1 = httpclient.execute(httpGet);
                HttpEntity entity1 = response1.getEntity();
                entity1.consumeContent();

                HttpPost httpPost = new HttpPost(BANNER_LOGIN_POST_URL);
                List <NameValuePair> nvps = new ArrayList <NameValuePair>();
                nvps.add(new BasicNameValuePair(BANNER_LOGIN_NAME, username));
                nvps.add(new BasicNameValuePair(BANNER_PASSWORD_NAME, password));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                HttpResponse response2 = httpclient.execute(httpPost);                                
                HttpEntity entity2 = response2.getEntity();
                entity2.consumeContent();
                                    
                CookieStore cookieStore = httpclient.getCookieStore();
                cookies = cookieStore.getCookies();
            }
            catch (IOException e)
            {
                Log.d(TAG,"Banner failed to respond with a valid HTTP response");
            } 
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // share cookies between httpclient and webview; 
            // Thank you stackoverflow.com!
            // http://stackoverflow.com/questions/1652850/android-webview-cookie-problem?lq=1
            if (cookies != null && !cookies.isEmpty()) {
                CookieSyncManager.createInstance(webView.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                for (Cookie cookie : cookies) {
                    Cookie sessionInfo = cookie;
                    String cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue() + "; domain=" + sessionInfo.getDomain();
                    cookieManager.setCookie(BANNER_DOMAIN, cookieString);
                    CookieSyncManager.getInstance().sync();
                }
            }
            SystemClock.sleep(2000);
            webView.loadUrl(BANNER_MENU_URL);
            
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

    }

}