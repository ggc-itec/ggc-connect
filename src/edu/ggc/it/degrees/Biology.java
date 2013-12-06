//*******************************************************************
// Class: Biology
//
// This class is used to retrieve and display the program plans
// for each program in the Biology major.
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

public class Biology extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biology);
		
		// Button that will open the program plan PDF for the Biochemistry track
		
		Button bioChem = (Button) findViewById(R.id.biochemistry);
		bioChem.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-bio-biochemistry.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Biochemistry Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Cell Biology track
		
		Button cellBio = (Button) findViewById(R.id.cellBio);
		cellBio.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-bio-cell-biology-biotech.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Cellular BIology Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the General Biology track

		Button genBio = (Button) findViewById(R.id.generalBio);
		genBio.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-bio-general2.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading General BIology Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Biology Teacher Certification track

		Button teacherCert = (Button) findViewById(R.id.teacherCert_bio);
		teacherCert.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-bio-teach-cert-update11-16.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Biology Teacher Certification Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});		
		
		// Button that will take the user back one page
		
		Button goBack = (Button) findViewById(R.id.biology_back);
		goBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Biology.this, ScienceTechnology.class);
				Biology.this.startActivity(intent);
				
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
		getMenuInflater().inflate(R.menu.biology, menu);
		return true;
	}

}
