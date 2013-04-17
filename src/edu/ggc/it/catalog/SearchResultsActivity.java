package edu.ggc.it.catalog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ggc.it.R;
import edu.ggc.it.banner.Course;
import edu.ggc.it.banner.CourseSearchBuilder;
import edu.ggc.it.banner.Instructor;
import edu.ggc.it.banner.Meeting;
import edu.ggc.it.banner.Section;
import android.app.ListActivity;
import android.content.Context;
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

public class SearchResultsActivity extends ListActivity {
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);
	
	private SectionAdapter sectionAdapter;
	private SectionClickListener sectionListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		CourseSearchBuilder searchBuilder = (CourseSearchBuilder)getIntent().getExtras().get(CourseSearchBuilder.KEY);
		sectionListener = new SectionClickListener();
		// the search may take some time, so initialize the list to empty
		List<Section> emptyList = new ArrayList<Section>();
		sectionAdapter = new SectionAdapter(this, emptyList);
		setListAdapter(sectionAdapter);
		
		Toast loadingNotice = Toast.makeText(this, "Loading...", Toast.LENGTH_LONG);
		loadingNotice.show();
		
		AsyncTask<CourseSearchBuilder, Void, List<Section>> task = new AsyncTask<CourseSearchBuilder, Void, List<Section>>(){

			@Override
			protected List<Section> doInBackground(CourseSearchBuilder... parms) {
				CourseSearchBuilder searchBuilder = parms[0];
				return searchBuilder.searchCourses(SearchResultsActivity.this);
			}
			
			@Override
			protected void onPostExecute(List<Section> sections){
				sectionAdapter.addAll(sections);
				sectionAdapter.notifyDataSetChanged();
			}
		};
		
		task.execute(searchBuilder);
	}
	
	/**
	 * Custom adapter for displaying previews of Sections.
	 * Design based on {@link http://www.codeproject.com/Articles/183608/Android-Lists-ListActivity-and-ListView-II-Custom}
	 * 
	 * @author Jacob
	 *
	 */
	private class SectionAdapter extends ArrayAdapter<Section>{
		private static final int MAX_DESCRIPTION = 100;
		
		private List<Section> sections;
		
		public SectionAdapter(Context context, List<Section> sections){
			super(context, R.layout.section_preview, sections);
			this.sections = sections;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = getLayoutInflater();
			
			if (convertView == null)
				convertView = inflater.inflate(R.layout.section_preview, null);
			
			// populate data
			TextView courseTitle = (TextView)convertView.findViewById(R.id.cs_course_title);
			TextView description = (TextView)convertView.findViewById(R.id.cs_desc_abbreviated);
			TextView hours = (TextView)convertView.findViewById(R.id.cs_hours_preview);
			TableLayout meetingTable = (TableLayout)convertView.findViewById(R.id.cs_meeting_preview_table);
			Section section = sections.get(position);
			Course course = section.getCourse();
			List<Meeting> meetings = section.getMeetings();
			
			courseTitle.setText(String.format("%s - %02d", course.getName(), section.getSection()));
			description.setText(course.getShortDescription(MAX_DESCRIPTION));
			hours.setText(Double.toString(course.getCredits()) + " Credit hours");
			
			for (int i = 0; i < meetings.size(); i++){
				Meeting meeting = meetings.get(i);
				View meetingRow = null;
				
				// the first row is a header, so skip it
				if (meetingTable.getChildCount() > i+1){
					meetingRow = meetingTable.getChildAt(i+1);
				} else{
					meetingRow = inflater.inflate(R.layout.meeting_preview, null);
					meetingTable.addView(meetingRow);
				}
				
				TextView daysCell = (TextView)meetingRow.findViewById(R.id.cs_days_preview);
				TextView timeRange = (TextView)meetingRow.findViewById(R.id.cs_time_preview);
				TextView instructorCell = (TextView)meetingRow.findViewById(R.id.cs_instructor_preview);
				Instructor instructor = meeting.getInstructor();
				String beginTime = TIME_FORMAT.format(meeting.getBeginTime());
				String endTime = TIME_FORMAT.format(meeting.getEndTime());
				String range = beginTime + " - " + endTime;
				String instructorName = null;
				if (instructor == null)
					instructorName = "TBA";
				else
					instructorName = instructor.getName();
				
				daysCell.setText(meeting.getDays());
				timeRange.setText(range);
				instructorCell.setText(instructorName);
			}
			
			if (meetingTable.getChildCount() > meetings.size()+1){
				for (int i = meetings.size()+1; i < meetingTable.getChildCount(); i++)
					meetingTable.getChildAt(i).setVisibility(View.GONE);
			}
			
			convertView.setTag(section);
			convertView.setOnClickListener(sectionListener);
			
			return convertView;
		}
	}
	
	private class SectionClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			Section section = (Section)view.getTag();
			
			Intent detailIntent = new Intent(SearchResultsActivity.this, SectionDetailActivity.class);
			detailIntent.putExtra(Section.KEY, section);
			startActivity(detailIntent);
		}
		
	}
}
