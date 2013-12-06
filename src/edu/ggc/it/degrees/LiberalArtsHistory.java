//*******************************************************************
// Class: LiberalArtsHistory
//
// This class is used to retrieve and display the program plans
// for each program in the Liberal Arts - History major.
//
// Created By: Danny Mansour, Marcus Rogers, and Christina Davis
//*******************************************************************

package edu.ggc.it.degrees;

import edu.ggc.it.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LiberalArtsHistory extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liberal_arts_history);
		
		// Button that will open the program plan PDF for the American History track
		
		Button americanHistory = (Button) findViewById(R.id.americanHistory);
		americanHistory.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-history-us-history.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading American History Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Interdisciplinary Studies track
		
		Button interStudies = (Button) findViewById(R.id.interDiscHistory);
		interStudies.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-history-thematic-interdisc.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Interdisciplinary Studies Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Western Civilization track
		
		Button westernCiv = (Button) findViewById(R.id.westernCiv);
		westernCiv.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-history-western-civilization.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Western Civilization Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the World History track
		
		Button worldHistory = (Button) findViewById(R.id.worldHistory);
		worldHistory.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-history-world-history.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading World History Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the History Teacher Certification track
		
		Button teacherCert = (Button) findViewById(R.id.teacherCertHistory);
		teacherCert.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-edu-history-teach-cert.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading History Teacher Certification Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user back one page
		
		Button goBack = (Button) findViewById(R.id.History_back);
		goBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(LiberalArtsHistory.this, LiberalArts.class);
				LiberalArtsHistory.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Taking You Back...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liberal_arts_history, menu);
		return true;
	}
}