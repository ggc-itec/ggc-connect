package edu.ggc.it.schedule;

import edu.ggc.it.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;

public class ScheduleAddClass extends Activity {
	
	private Context scheduleContext;
	
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_add_class);
		scheduleContext = this;
		
		btnCancel = (Button) findViewById(R.id.btn_schedule_add_cancel);
		btnCancel.setOnClickListener(new scheduleAddListener());
	}
	
	public class scheduleAddListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.btn_schedule_add_cancel) {
				finish();
			}
		}
		
	}

}
