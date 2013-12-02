package edu.ggc.it.catalog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ggc.it.R;
import edu.ggc.it.banner.Course;
import edu.ggc.it.banner.CourseSearchBuilder;
import edu.ggc.it.banner.Instructor;
import edu.ggc.it.banner.Meeting;
import edu.ggc.it.banner.Section;
import edu.ggc.it.schedule.ScheduleUpdateActivity;
import edu.ggc.it.schedule.helper.ClassItem;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsActivity extends ListActivity
{
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);
	private SectionAdapter sectionAdapter;
	private SectionClickListener sectionListener;

	@Override
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);

		CourseSearchBuilder searchBuilder = (CourseSearchBuilder) getIntent().getExtras().get(CourseSearchBuilder.KEY);
		sectionListener = new SectionClickListener();

		List<Section> emptyList = new ArrayList<Section>();
		sectionAdapter = new SectionAdapter(this, emptyList);
		setListAdapter(sectionAdapter);

		Toast loadingNotice = Toast.makeText(this, "Loading...", Toast.LENGTH_LONG);
		loadingNotice.show();

		AsyncTask<CourseSearchBuilder, Void, List<Section>> task = new AsyncTask<CourseSearchBuilder, Void, List<Section>>() {

			@Override
			protected List<Section> doInBackground(CourseSearchBuilder... parms)
            {
				CourseSearchBuilder searchBuilder = parms[0];
				return searchBuilder.searchCourses(SearchResultsActivity.this);
			}

			@Override
			protected void onPostExecute(List<Section> sections)
            {
				sectionAdapter.addAll(sections);
				sectionAdapter.notifyDataSetChanged();
			}
		};

		task.execute(searchBuilder);
	}

	/**
	 * Custom adapter for displaying previews of Sections. Design based on
	 * {@link http
	 * ://www.codeproject.com/Articles/183608/Android-Lists-ListActivity
	 * -and-ListView-II-Custom}
	 * 
	 * @author Jacob
	 * 
	 */
	private class SectionAdapter extends ArrayAdapter<Section>
    {
		private static final int MAX_DESCRIPTION = 100;
		private List<Section> sections;

		public SectionAdapter(Context context, List<Section> sections)
        {
			super(context, R.layout.section_preview, sections);
			this.sections = sections;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
        {
			LayoutInflater inflater = getLayoutInflater();

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.section_preview, null);
            }

			TextView courseTitle = (TextView) convertView.findViewById(R.id.cs_course_title);
			TextView description = (TextView) convertView.findViewById(R.id.cs_desc_abbreviated);
			TextView hours = (TextView) convertView.findViewById(R.id.cs_hours_preview);
			TableLayout meetingTable = (TableLayout) convertView.findViewById(R.id.cs_meeting_preview_table);
			Section section = sections.get(position);
			Course course = section.getCourse();
			List<Meeting> meetings = section.getMeetings();

			courseTitle.setText(String.format("%s - %02d", course.getName(), section.getSection()));
			description.setText(course.getShortDescription(MAX_DESCRIPTION));
			hours.setText(Double.toString(course.getCredits()) + " Credit hours");

			for (int i = 0; i < meetings.size(); i++) {
				Meeting meeting = meetings.get(i);
				View meetingRow = null;

				if (meetingTable.getChildCount() > i + 1) {
					meetingRow = meetingTable.getChildAt(i + 1);
				} else {
					meetingRow = inflater.inflate(R.layout.meeting_preview, null);
					meetingTable.addView(meetingRow);
				}

				TextView daysCell = (TextView) meetingRow.findViewById(R.id.cs_days_preview);
				TextView timeRange = (TextView) meetingRow.findViewById(R.id.cs_time_preview);
				TextView instructorCell = (TextView) meetingRow.findViewById(R.id.cs_instructor_preview);
				Instructor instructor = meeting.getInstructor();
				String beginTime = TIME_FORMAT.format(meeting.getBeginTime());
				String endTime = TIME_FORMAT.format(meeting.getEndTime());
				String range = beginTime + " - " + endTime;
				String instructorName = null;
				if (instructor == null) {
					instructorName = "TBA";
                } else {
					instructorName = instructor.getName();
                }

				daysCell.setText(meeting.getDays());
				timeRange.setText(range);
				instructorCell.setText(instructorName);
			}

			if (meetingTable.getChildCount() > meetings.size() + 1) {
				for (int i = meetings.size() + 1; i < meetingTable.getChildCount(); i++)
					meetingTable.getChildAt(i).setVisibility(View.GONE);
			}

			convertView.setTag(section);
			convertView.setOnClickListener(sectionListener);

			return convertView;
		}
	}

	private class SectionClickListener implements OnClickListener
    {
		@Override
		public void onClick(View view) {
			final Section section = (Section) view.getTag();

			new AlertDialog.Builder(SearchResultsActivity.this)
					.setTitle("Choose Action")
					.setItems(R.array.cs_course_actions,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										showSectionDetail(section);
									} else if (which == 1) {
										List<Meeting> meetings = section.getMeetings();
										if (meetings.size()==2) {
											showMeetingSelectionDialog(section);
										} else {
											addToSchedule(section, 0);
										}
									}
								}
							}).show();
		}

		private void showSectionDetail(Section section)
        {
			Intent detailIntent = new Intent(SearchResultsActivity.this, SectionDetailActivity.class);
			detailIntent.putExtra(Section.KEY, section);
			startActivity(detailIntent);
		}

		private void showMeetingSelectionDialog(final Section section)
        {
			List<Meeting> meetings = section.getMeetings();
			String[] meetingsArray = new String[meetings.size()];
			
			Meeting classTime = meetings.get(0);
			String classBeginTime = TIME_FORMAT.format(classTime.getBeginTime());
			String classEndTime = TIME_FORMAT.format(classTime.getEndTime());
			String classMeetingStr = "(Class) " + classTime.getDays() + " " + classBeginTime + " - "
					+ classEndTime;
			meetingsArray[0] = classMeetingStr;
			
			Meeting labTime = meetings.get(1);
			String labBeginTime = TIME_FORMAT.format(labTime.getBeginTime());
			String labEndTime = TIME_FORMAT.format(labTime.getEndTime());
			String labMeetingStr = "(Lab) " + labTime.getDays() + " " + labBeginTime + " - " + labEndTime;
			meetingsArray[1] = labMeetingStr;

			new AlertDialog.Builder(SearchResultsActivity.this)
					.setTitle("Add Class or Lab to Schedule")
					.setItems(meetingsArray,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									addToSchedule(section, which);
								}
							}).show();
		}

		private void addToSchedule(Section section, int selectedMeeting)
        {
			ClassItem ci = new ClassItem();
			Course course = section.getCourse();
			ci.setClassName(course.getName());
			ci.setSection(String.format("%02d", section.getSection()));

			List<Meeting> meetings = section.getMeetings();
			Meeting m = meetings.get(selectedMeeting);
			SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);
			SimpleDateFormat minuteFormat = new SimpleDateFormat("mm", Locale.US);

			Date beginTime = m.getBeginTime();
			ci.setStartTimeHour(Integer.parseInt(hourFormat.format(beginTime)));
			ci.setStartTimeMinute(Integer.parseInt(minuteFormat.format(beginTime)));

			Date endTime = m.getEndTime();
			ci.setEndTimeHour(Integer.parseInt(hourFormat.format(endTime)));
			ci.setEndTimeMinute(Integer.parseInt(minuteFormat.format(endTime)));

			char[] days = m.getDays().toCharArray();
			for (int i = 0; i < days.length; i++) {
				if (days[i] == 'M') {
					ci.setOnMonday(true);
				} else if (days[i] == 'T') {
					ci.setOnTuesday(true);
				} else if (days[i] == 'W') {
					ci.setOnWednesday(true);
				} else if (days[i] == 'R') {
					ci.setOnThursday(true);
				} else if (days[i] == 'F') {
					ci.setOnFriday(true);
				} else if (days[i] == 'S') {
					ci.setOnSaturday(true);
				}
			}

			String location = m.getLocation();
			ci.setBuildingLocation(location.substring(0, 10));
			ci.setRoomLocation(location.substring(11, location.length()));

			Intent scheduleIntent = new Intent(SearchResultsActivity.this, ScheduleUpdateActivity.class);
			scheduleIntent.putExtra("action", ScheduleUpdateActivity.ACTION_ADD_FROM_BANNER);
			scheduleIntent.putExtra("class", ci);
			startActivity(scheduleIntent);
		}
	}
}
