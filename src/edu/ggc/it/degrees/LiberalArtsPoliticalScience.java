//*******************************************************************
// Class: LiberalArtsPoliticalScience
//
// This class is used to retrieve and display the program plans
// for each program in the Liberal Arts - Political Science major.
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

public class LiberalArtsPoliticalScience extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liberal_arts_political_science);
		
		// Button that will open the program plan PDF for the American Government track
		
		Button americanGov = (Button) findViewById(R.id.american_government);
		americanGov.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-poli-sci-american-gov.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading American Government Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Comparative Politics track
		
		Button comparativePol = (Button) findViewById(R.id.comparative_politics);
		comparativePol.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-poli-sci-comp-pol.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Comparative Politics Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the International Relations track

		Button interRelations = (Button) findViewById(R.id.international_relations);
		interRelations.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-poli-sci-intl-rel.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading International Relations Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Legal Studies track
		
		Button legalStudies = (Button) findViewById(R.id.legal_studies);
		legalStudies.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-poli-sci-legal-studies.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Legal Studies Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will open the program plan PDF for the Political Science Teacher Certification track
		
		Button teacherCert = (Button) findViewById(R.id.teacher_cert);
		teacherCert.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{	
				String pdfurl = "http://www.ggc.edu/about-ggc/departments/registrar/docs/program-plans/2011-2012/2011-12-poli-sci-legal-studies.pdf";
				String googleDocsUrl = "http://docs.google.com/viewer?url=" + pdfurl;
				Intent pdf_intent = new Intent(Intent.ACTION_VIEW);
				pdf_intent.setDataAndType(Uri.parse(googleDocsUrl), "text/html");
				startActivity(pdf_intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading Political Science Teacher Certification Program...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
						
		// Button that will take the user back one page
		
		Button goBack = (Button) findViewById(R.id.politics_back);
		goBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(LiberalArtsPoliticalScience.this, LiberalArts.class);
				LiberalArtsPoliticalScience.this.startActivity(intent);
				
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
		getMenuInflater().inflate(R.menu.liberal_arts_political_science, menu);
		return true;
	}
}