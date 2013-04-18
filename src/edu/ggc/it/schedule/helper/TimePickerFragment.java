package edu.ggc.it.schedule.helper;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * This dialog fragment is used to create a timepicker popup
 * @author Raj Ramsaroop
 *
 */
public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	private OnTimeSetListener mListener;
	private Bundle args;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		args = this.getArguments();
		int hourToSet = args.getInt("hour");
		int minuteToSet = args.getInt("minute");
		final Calendar c = Calendar.getInstance();
		// use current time for hour and minute if no time set already
		int hour = (hourToSet < 24) ? hourToSet : c.get(Calendar.HOUR_OF_DAY);
		int minute = (minuteToSet < 60) ? minuteToSet : c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	/**
	 * Determines what to do when the time is set
	 */
	public void onTimeSet(TimePicker view, int hour, int minute) {
		int buttonSource = args.getInt("buttonSource");
		mListener.onTimeSet(buttonSource, hour, minute);
	}

	/**
	 * An abstract interface that any class/activity that uses the dialog fragment
	 * must implement so that they can choose what happens when the time is set
	 * @author Raj Ramsaroop
	 *
	 */
	public static interface OnTimeSetListener {
		public abstract void onTimeSet(int buttonSource, int hour, int minute);
	}

	/**
	 * This method makes sure the OnTimeSetListener is attached when the use
	 * the dialog fragment.
	 */
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mListener = (OnTimeSetListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTimeSetListener");
		}
	}
}
