package edu.ggc.it.catalog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.ggc.it.R;
import edu.ggc.it.banner.Banner;
import edu.ggc.it.banner.Course;
import edu.ggc.it.banner.Instructor;
import edu.ggc.it.banner.Meeting;
import edu.ggc.it.banner.Section;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

public class SectionDetailActivity extends Activity {
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy", Locale.US);
	
	private LinearLayout detailLayout;
	private TextView detailTitle;
	private TextView detailDesc;
	private TextView detailHours;
	private LinearLayout loadingLayout;
	private TextView detailLoading;
	private ProgressBar detailProgress;
	private TableLayout enrollmentTable;
	private TextView capacityText;
	private TextView actualText;
	private TextView remainingText;
	
	private Section section;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_detail);
		
		// find views
		detailLayout = (LinearLayout)findViewById(R.id.cs_detail_layout);
		detailTitle = (TextView)findViewById(R.id.cs_detail_title);
		detailDesc = (TextView)findViewById(R.id.cs_detail_desc);
		detailHours = (TextView)findViewById(R.id.cs_detail_hours);
		loadingLayout = (LinearLayout)findViewById(R.id.cs_loading_layout);
		detailLoading = (TextView)findViewById(R.id.cs_detail_loading);
		detailProgress = (ProgressBar)findViewById(R.id.cs_detail_progress);
		enrollmentTable = (TableLayout)findViewById(R.id.cs_enrollment_table);
		capacityText = (TextView)findViewById(R.id.cs_capacity_text);
		actualText = (TextView)findViewById(R.id.cs_actual_text);
		remainingText = (TextView)findViewById(R.id.cs_remaining_text);
		
		section = (Section)getIntent().getExtras().get(Section.KEY);
		Course course = section.getCourse();
		
		detailTitle.setText(String.format("%s - %02d", course.getName(), section.getSection()));
		detailDesc.setText(course.getDescription());
		detailHours.setText(Double.toString(course.getCredits()) + " Credit hours");
		
		List<Meeting> meetings = section.getMeetings();
		LayoutInflater inflater = getLayoutInflater();
		for (Meeting meeting: meetings){
			View meetingDetail = inflater.inflate(R.layout.meeting_detail, null);
			TextView typeText = (TextView)meetingDetail.findViewById(R.id.cs_type_text);
			TextView daysDetail = (TextView)meetingDetail.findViewById(R.id.cs_days_detail);
			TextView dateDetail = (TextView)meetingDetail.findViewById(R.id.cs_date_detail);
			TextView timeDetail = (TextView)meetingDetail.findViewById(R.id.cs_time_detail);
			TextView locationDetail = (TextView)meetingDetail.findViewById(R.id.cs_location_detail);
			TextView instructorDetail = (TextView)meetingDetail.findViewById(R.id.cs_instructor_detail);
			
			String beginDate = DATE_FORMAT.format(meeting.getBeginDate());
			String endDate = DATE_FORMAT.format(meeting.getEndDate());
			String beginTime = TIME_FORMAT.format(meeting.getBeginTime());
			String endTime = TIME_FORMAT.format(meeting.getEndTime());
			Instructor instructor = meeting.getInstructor();
			
			typeText.setText(meeting.getType());
			daysDetail.setText(meeting.getDays());
			dateDetail.setText(String.format("%s - %s", beginDate, endDate));
			timeDetail.setText(String.format("%s - %s", beginTime, endTime));
			locationDetail.setText(meeting.getLocation());
			instructorDetail.setText((instructor == null? "TBA": instructor.getName()));
			
			detailLayout.addView(meetingDetail);
		}
		
		AsyncTask<String, Void, Integer[]> task = new AsyncTask<String, Void, Integer[]>(){

			@Override
			protected Integer[] doInBackground(String... args) {
				String term = args[0];
				String crn = args[1];
				
				return Banner.getSectionEnrollment(term, crn);
			}
			
			@Override
			protected void onPostExecute(Integer[] enrollment){
				if (enrollment != null){
					capacityText.setText(enrollment[0].toString());
					actualText.setText(enrollment[1].toString());
					remainingText.setText(enrollment[2].toString());
					loadingLayout.setVisibility(View.GONE);
					enrollmentTable.setVisibility(View.VISIBLE);
				} else{
					detailLoading.setText(R.string.cs_loading_error);
					detailProgress.setVisibility(View.GONE);
				}
			}
		};
		
		task.execute(section.getTerm(), Integer.toString(section.getCRN()));
	}
}
