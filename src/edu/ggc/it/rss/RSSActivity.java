package edu.ggc.it.rss;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import edu.ggc.it.R;
import edu.ggc.it.rss.RSSTask.RSSTaskComplete;

/** CLASS: RSSActivity
 * Activity responsible for displaying various GGC related RSS feeds.
 * This class has two inner classes their order on this page is a hint to their view hierarchy within the app.
 * From bottom to top:
 * 1. RSSActivity,
 * 2. RSSListFragment (returned by RSSPagerAdapter)
 * 3. Views returned by RSSListAdapter
 * @author crystalist, Derek
 * 
 */
public class RSSActivity extends FragmentActivity implements RSSTaskComplete
{
    private static final RSSFeed[] FEEDS = RSSFeed.values();
    private static final int NUM_FEEDS = FEEDS.length;

    private ViewPager pager;
    private RSSPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	
	//sets this window to be hardware accelerated, must be done before setContentView()
	//check if Build is API level 11 or newer, hardware acceleration was added in Honeycomb (API 11)
	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	
	setContentView(R.layout.rss_view_pager);
	
	setUpDatabase();
	
	Context context = this;
	
	new RSSTask(this, context, true).execute(FEEDS);//create & execute RSSTask
	setTitle("GGC Feeds");
	pager = (ViewPager) findViewById(R.id.rss_pager);
	pagerAdapter = new RSSPagerAdapter(getSupportFragmentManager());
    }
    
    /**
     * Simply sets up the database to be used in the rss package
     */
    private void setUpDatabase()
    {
	RSSDatabase db = RSSDatabase.getInstance(this);
	db.onUpgrade(db.getWritableDatabase(), 0, 0);
    }

    /**
     * Called when RSSTask finishes execution
     */
    @Override
    public void taskComplete()
    {
	pager.setAdapter(pagerAdapter);
    }
    
    /** CLASS: RSSPagerAdapter
     * This Adapter returns Fragments that will be added to RSSActivity.
     * Think of these Fragments as pages in a book, each Fragment is a list for each RSSFeed.
     * @author Derek
     *
     */
    public static class RSSPagerAdapter extends FragmentStatePagerAdapter
    {
	public RSSPagerAdapter(FragmentManager fm)
	{
	    super(fm);
	}

	/**
	 * Returns a Fragment at specified index.
	 * In this case the Fragments are ListFragments one for each RSSFeed.
	 */
	@Override
	public Fragment getItem(int index)
	{
	    Fragment fragment = new RSSListFragment();
	    Bundle args = new Bundle();
	    args.putInt(RSSListFragment.FEED_INDEX_TAG, index);
	    fragment.setArguments(args);
	    return fragment;
	}

	/**
	 * Returns the number of Fragments this adapter will handle.
	 */
	@Override
	public int getCount()
	{
	    return NUM_FEEDS;
	}

	/**
	 * Sets the title for each Fragment. Displayed in a title strip.
	 */
	@Override
	public CharSequence getPageTitle(int position)
	{
	    return FEEDS[position].title();
	}
    }
    
    /** CLASS: RSSListFragment
     * This class inflates and returns a View (specifically ListView) to RSSPagerAdapter.
     * Each ListView sets a RSSListAdapter to get Views from.
     * @author Derek
     *
     */
    public static class RSSListFragment extends ListFragment
    {
	public static final String FEED_INDEX_TAG = "edu.ggc.it.rss.RSSListFragment.FEED_INDEX_TAG";
	private RSSListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    View list = inflater.inflate(R.layout.rss_list, container, false);
	    int index = getArguments().getInt(FEED_INDEX_TAG);
	    Context context = this.getActivity();
	    adapter = new RSSListAdapter(context, FEEDS[index]);
	    setListAdapter(adapter);
	    return list;
	}

	/**
	 * Called when user clicks on a List item. Opens web page of item clicked.
	 * 
	 * @param list		ListView where the click happened
	 * @param view		View that was clicked within the ListView
	 * @param position		position of the View item clicked
	 * @param id		row id of the item that was clicked 
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
	    String link = (String) adapter.getLinkAt(position);
	    Uri uri = Uri.parse(link);
	    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    startActivity(intent);
	}
	
	/**
	 * Clean up by closing Cursor object in RSSListAdapter.
	 */
	@Override
	public void onDestroy()
	{
	    adapter.closeCursor();
	    super.onDestroy();
	}
    }
}
