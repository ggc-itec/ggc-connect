package edu.ggc.it.d2l_web;

import edu.ggc.it.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


/*
 * Activity that logs in and displays D2L in a WebView
 * 
 * D2L and Banner have different login mechanism and therefore have login tasks that are slightly different
 */
public class D2LWebActivity extends Activity
{
    public static final String D2L_DOMAIN = "ggc.view.usg.edu";
    public static final String D2L_LOGIN_URL = "https://ggc.view.usg.edu";
    public static final String D2L_LOGIN_POST_URL = "https://ggc.view.usg.edu/d2l/lp/auth/login/login.d2l";
    public static final String D2L_MENU_URL = "https://ggc.view.usg.edu/";
    public static final String D2L_LOGIN_NAME = "userName";
    public static final String D2L_PASSWORD_NAME = "password";
    private static List<Cookie> cookies;
    private WebView webView;
    private String username;
    private String password;
    private String TAG = "D2LWebActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2l_web);
        Intent intent = getIntent();
        username = intent.getExtras().getString(D2L_LOGIN_NAME);
        password = intent.getExtras().getString(D2L_PASSWORD_NAME);
        webView = (WebView) findViewById(R.id.webview_d2l);
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
        D2LLoginTask task = new D2LLoginTask(D2LWebActivity.this);
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

    private class D2LLoginTask extends AsyncTask<Void, Void, String> {
 
        private ProgressDialog dialog;
        private String str = "";
        
        public D2LLoginTask(Context context) {
            dialog = new ProgressDialog(context);
        }
        
        protected void onPreExecute() {
            dialog.setMessage("Logging in...");
            dialog.show();
        };
        
        @Override
        protected String doInBackground(Void... params) {
            
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                
                HttpGet httpGet = new HttpGet(D2L_LOGIN_URL);
                HttpResponse response1 = null;
                response1 = httpclient.execute(httpGet);
                HttpEntity entity1 = response1.getEntity();
                entity1.consumeContent();

                HttpPost httpPost = new HttpPost(D2L_LOGIN_POST_URL);
                List <NameValuePair> nvps = new ArrayList <NameValuePair>();
                nvps.add(new BasicNameValuePair(D2L_LOGIN_NAME, username));
                nvps.add(new BasicNameValuePair(D2L_PASSWORD_NAME, password));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                HttpResponse response2 = httpclient.execute(httpPost);                                
     
                CookieStore cookieStore = httpclient.getCookieStore();
                cookies = cookieStore.getCookies();
                
                InputStream ins = response2.getEntity().getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(ins));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                {
                    str = str + inputLine;
                }
                HttpEntity entity2 = response2.getEntity();
                entity2.consumeContent();
            }
            catch (IOException e)
            {
                Log.d(TAG,"D2L failed to respond with a valid HTTP response");
            } 
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            // how to share cookies between httpclient and webview; http://stackoverflow.com/questions/1652850/android-webview-cookie-problem?lq=1
            if (cookies != null && !cookies.isEmpty()) {
                CookieSyncManager.createInstance(webView.getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                for (Cookie cookie : cookies) {
                    Cookie sessionInfo = cookie;
                    String cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue() + "; domain=" + sessionInfo.getDomain();
                    cookieManager.setCookie(D2L_DOMAIN, cookieString);
                    CookieSyncManager.getInstance().sync();
                }
            }
            SystemClock.sleep(2000);
            webView.loadDataWithBaseURL(D2L_LOGIN_URL, result, "text/html", "UTF-8", null);

            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

    }

}