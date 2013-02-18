package edu.ggc.it.directory;

import edu.ggc.it.R;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DirectoryActivity extends ListActivity {
	
	//a first commit
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ggcdirectory);
		
		setListAdapter(new ArrayAdapter<String>(this,
			    android.R.layout.simple_list_item_1, getResources()
			      .getStringArray(R.array.parent_directories)));
	}

}
