package edu.ggc.it.about;

import java.io.IOException;
import edu.ggc.it.R;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Class GGCSongActivity
 * 
 * Media player that plays GGC's Alma Mater (Official Song) and displays lyrics.
 *
 */
public class GGCSongActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener
{
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private MediaPlayer mp;
	private Handler mHandler = new Handler();
	private int seekForwardTime = 5000;
	private int seekBackwardTime = 5000;
	private AssetFileDescriptor songFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ggcsong);
		
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		songProgressBar.setOnSeekBarChangeListener(this);
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		songFile = this.getResources().openRawResourceFd(R.raw.ggc_alma_mater); 
		
		prepareMediaPlayer();
		
		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// check for already playing
				if(mp.isPlaying())
				{
					if(mp!=null)
					{
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}
				else
				{
					// Resume song
					if(mp!=null)
					{
						mp.start();
						
						// Changes button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
					else
					{
						prepareMediaPlayer();
					}
				}
			}
		});
		
		/**
		 * Forward button click event
		 * Forwards song specified seconds
		 * */
		btnForward.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// gets current song position				
				int currentPosition = mp.getCurrentPosition();
				
				// check if seekForward time is lesser than song duration
				if(currentPosition + seekForwardTime <= mp.getDuration())
				{
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				}
				else
				{
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
			}
		});
		
		/**
		 * Rewind button click event
		 * Rewinds song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0)
				{
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				}
				else
				{
					// backward to starting position
					mp.seekTo(0);
				}
			}
		});
	}
	
	/**
	 * Function to set up media player
	 * */
	public void prepareMediaPlayer()
	{
		try 
		{
			// sets media player's data source and prepares it
			mp.reset();
			mp.setDataSource(songFile.getFileDescriptor());
			mp.prepare();
			
			// Changes Button Image to play image
			btnPlay.setImageResource(R.drawable.btn_play);
			
			// sets Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(mp.getDuration());
			
			// Displays Total Duration time
			int totalDuration = mp.getDuration();
			songTotalDurationLabel.setText(milliSecondsToTimer(totalDuration));
			
			// Updates progress bar
			updateProgressBar();			
		}
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalStateException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() 
	{
		mHandler.postDelayed(mUpdateTimeTask, 100);        
	}
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() 
	{
		public void run() 
		{
			// gets current song position
			int currentDuration = mp.getCurrentPosition();
			
			// Displays time completed playing
			songCurrentDurationLabel.setText(milliSecondsToTimer(currentDuration));
			
			// Updating progress bar
			songProgressBar.setProgress(currentDuration);
			
			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};
	
	/**
	 * Updates media player to play from where user sets the seekbar.
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
	{
		if (fromUser) 
		{
			mp.seekTo(progress);
		}
	}
	
	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 
	{
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
	
	/**
	 * When user stops moving the progress handler
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) 
	{
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
		
		// sets current song position to where user sets the seekbar
		int currentPosition = mp.getCurrentPosition();
		
		// forwards or rewinds to where user set the seekbar
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		//mp.pause();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mp.release();
		btnPlay.setImageResource(R.drawable.btn_play);
		finish();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mp.release();
	}
	
	@Override
	public void onCompletion(MediaPlayer mp)
	{
		btnPlay.setImageResource(R.drawable.btn_play);
	}
	
	public void onStop()
	{
		super.onStop();
		mp.release();
	}
	
	/**
	 * Function to convert milliseconds time to Timer Format for media player timer
	 * Minutes:Seconds
	 * */
	public String milliSecondsToTimer(int milliseconds)
	{
		String strTime = "";
		String strSeconds = "";
		
		// Convert total duration into time
		int minutes = (milliseconds % (1000*60*60)) / (1000*60);
		int seconds = ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		
		// Prepending 0 to seconds if it is one digit
		if(seconds < 10)
		{ 
			strSeconds = "0" + seconds;
		}
		else
		{
			strSeconds = "" + seconds;
		}
		
		strTime += + minutes + ":" + strSeconds;
		
		// return timer string
		return strTime;
	}
	
}
