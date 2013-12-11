package edu.ggc.it.mainscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSActivity;
import edu.ggc.it.rss.RSSFeed;

/**
 * Created by gregwesterfield on 10/21/13.
 */
public class MainScreenSocialView extends LinearLayout implements View.OnClickListener
{
    private Context context;

    public MainScreenSocialView(Context context)
    {
        super(context);
        init(context);
    }

    public MainScreenSocialView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_screen_social_view, this, true);

        ImageButton facebookButton = getListenedImageButton(R.id.facebook_page);
        ImageButton twitterButton = getListenedImageButton(R.id.twitter_page);
        ImageButton youtubeButton = getListenedImageButton(R.id.youtube_page);
        ImageButton rssButton = getListenedImageButton(R.id.rss_feed);
    }

    private ImageButton getListenedImageButton(final int resource)
    {
        ImageButton temp = (ImageButton) findViewById(resource);
        temp.setOnClickListener(this);
        return temp;
    }

    @Override
    public void onClick(View view)
    {
       if (view.getId() == R.id.facebook_page) {
            try {
                context.getPackageManager().getPackageInfo(
                        "com.facebook.katana", 0);
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://profile/78573401446")));
            } catch (Exception e) {
                context.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.facebook.com/georgiagwinnett")));
            }

        } else if (view.getId() == R.id.twitter_page) {
            try {
                context.getPackageManager().getPackageInfo(
                        "com.twitter.android", 0);
                context.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("twitter://user?screen_name=georgiagwinnett")));
            } catch (Exception e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/georgiagwinnett")));
            }
        } else if (view.getId() == R.id.youtube_page) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri
                        .parse("http://www.youtube.com/user/georgiagwinnett"));
                context.startActivity(intent);
            } catch (Exception e) {
                context.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/user/georgiagwinnett")));
            }
        } else if (view.getId() == R.id.rss_feed) {
            context.startActivity(new Intent(context, RSSActivity.class));
        }
    }
}
