package edu.ggc.it.directory;


import edu.ggc.it.DirectorySearchWebView;
import edu.ggc.it.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class DirectoryActivity extends Activity {
	
	private TabHost tabHost;
	private Intent intent;
	private ListView list;
	public final static String EXTRA_MESSAGE = "edu.ggc.it.directory.MESSAGE";
	public final static String lastName = "edu.ggc.it.directory.MESSAGE";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent().setClass(this, DirectoryActivity.class);
		setContentView(R.layout.activity_ggcdirectory);
		list = (ListView) findViewById(R.id.directory_listView);
		list.setAdapter(new ArrayAdapter<String>(this,
		       android.R.layout.simple_list_item_1, getResources()
		         .getStringArray(R.array.parent_directories)));
		 

        tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		TabSpec spec1 = tabHost.newTabSpec("First Tab");
		spec1.setIndicator("Departments");
		spec1.setContent(R.id.tab1);
		
		TabSpec spec2 = tabHost.newTabSpec("Second Tab");
		spec2.setIndicator("Name Search");
		spec2.setContent(R.id.tab2);
		

		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
	}
	
	public void searchName(View view) {
		Intent intent = new Intent(this, DirectorySearchWebView.class);
		EditText editText = (EditText) findViewById(R.id.firstNameText);
		EditText editText2 = (EditText) findViewById(R.id.lastNameText);
		String message = editText.getText().toString();
		String message2 = editText2.getText().toString();
		String wholeUrl = "http://www.ggc.edu/about-ggc/directory?firstname=" + message +"&firstname_modifier=like&lastname="+message2+"&lastname_modifier=like&search=Search";
		intent.putExtra(EXTRA_MESSAGE, wholeUrl);
		startActivity(intent);
	}

}
