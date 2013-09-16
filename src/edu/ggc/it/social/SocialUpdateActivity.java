package edu.ggc.it.social;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import edu.ggc.it.R;

/**
 * 
 * This class is responsible for updating the social activity.
 * @author joe
 *
 */
public class SocialUpdateActivity extends Activity {

	private Button buttonCancel;
	private Button buttonSubmit;
	private EditText editTextName;
	private EditText editTextSubject;
	private EditText editTextBody;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_update);
		buttonCancel = (Button) findViewById(R.id.button_social_cancel);
		buttonSubmit = (Button) findViewById(R.id.button_social_submit);
		editTextName = (EditText) findViewById(R.id.edittext_social_name);
		editTextSubject = (EditText) findViewById(R.id.edittext_social_subject);
		editTextBody = (EditText) findViewById(R.id.edittext_social_body);
		context = this;

		Intent intent = getIntent();
		String subject = intent
				.getStringExtra(SocialListActivity.REPLY_SUBJECT);
		String body = intent.getStringExtra(SocialListActivity.REPLY_BODY);
		if ( subject != null && body != null) {
			editTextSubject.setText("Re: " + subject);
			editTextBody.setText("\n----------------- \n" + body);
		}
		
		editTextBody.setSelectAllOnFocus(true);
		
		editTextBody.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editTextBody.setSelection(0);
			}
		});
		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		buttonSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editTextName.getText().toString().equals("")
						|| editTextSubject.getText().toString().equals("")
						|| editTextSubject.getText().toString().equals("")) {

					new AlertDialog.Builder(context)
							.setTitle("Blank Fields")
							.setMessage(
									"Please enter all the information in the blanks")
							.setNeutralButton("Close",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).show();

				} else {
					uploadParse();
				}
			}
		});
	}

	// uploads name, subject, data and body of the message
	private void uploadParse() {
		Parse.initialize(this, getString(R.string.parse_app_id),getString(R.string.parse_client_key));
		ParseObject testObject = new ParseObject("ggcconnect");
		testObject.put("name", editTextName.getText().toString());
		String mydate = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		testObject.put("date", mydate);
		testObject.put("subject", editTextSubject.getText().toString());
		testObject.put("body", editTextBody.getText().toString());
		testObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException arg0) {
				finish();
			}
		});
		Toast.makeText(context, "Thank you for your post", Toast.LENGTH_SHORT)
				.show();
	}

}
