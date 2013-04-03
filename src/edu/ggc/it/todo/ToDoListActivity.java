package edu.ggc.it.todo;

import edu.ggc.it.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Sets up a list view of ToDo items
 * 
 * @author crystalist
 * 
 */
public class ToDoListActivity extends ListActivity {

	private ToDoDatabase database;
	private SimpleCursorAdapter cursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);

		Button button = (Button) findViewById(R.id.addTaskButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDatabase(0, true);
			}
		});
		database = new ToDoDatabase(this);
		database.open();

		Cursor cursor = database.queryAllByRowID();
		startManagingCursor(cursor);

		String[] from = new String[] { ToDoDatabase.KEY_TASK };
		int[] to = new int[] { R.id.taskEntry };
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row,
				cursor, from, to);

		setListAdapter(cursorAdapter);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

	public void remove(long rowId) {
		database.deleteRow(rowId);
		cursorAdapter.getCursor().requery();
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position,
			long rowId) {
		super.onListItemClick(list, view, position, rowId);
		updateDatabase(rowId, false);
	}

	public void updateDatabase(long rowId, boolean create) {
		Intent intent = new Intent(this, ToDoUpdateActivity.class);
		if (!create) {
			intent.putExtra("rowID", rowId);
		}
		startActivity(intent);
	}
}
