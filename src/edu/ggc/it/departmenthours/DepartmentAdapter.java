package edu.ggc.it.departmenthours;

import java.util.List;

import edu.ggc.it.R;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Class: DepartmentAdapter
 * 
 * This class is a base adapter that creates a view for each
 * of the departments in the list activity displaying the
 * department's name, if the department is currently open or closed,
 * and its opening and closing times.
 *
 */
public class DepartmentAdapter extends BaseAdapter
{
	private Context context;
	private List<Department> departments;
		
	/**
	 * Constructor that accepts the context where the views should place
	 * and the departments to populate the views
	 * @param context the context where the views will display
	 * @param departments the list of departments to populate the views
	 */
	public DepartmentAdapter(Context context, List<Department> departments)
	{
		this.context = context;
		this.departments = departments;
	}
	
	@Override
	/**
	 * @return the number of items (departments) in the adapter
	 */
	public int getCount()
	{
		return departments.size();
	}

	@Override
	/**
	 * @param position the position of the data in the data set
	 * @return the department item in this position in the data set
	 */
	public Object getItem(int position)
	{
		//return departments.get(position);
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Returns a view for each of the department items
	 * @param position the current position for which the view is being generated
	 * @param convertView a saved view from last run, cannot be reused
	 * @param parent where the generated view will be placed
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout row = new LinearLayout(context);
		View.inflate(context, R.layout.activity_depthours_detail, row);
		
		TextView nameText = (TextView) row.findViewById(R.id.depthours_name);
		TextView openClosedText = (TextView) row.findViewById(R.id.depthours_open_closed);
		TextView hoursText = (TextView) row.findViewById(R.id.depthours_hours);

		nameText.setText(departments.get(position).getName());
		
		if (departments.get(position).isOpen())
		{
			openClosedText.setText("OPEN");
			openClosedText.setTextColor(0xff00ff00);
		}
		else
		{
			openClosedText.setText("CLOSED");
			openClosedText.setTextColor(0xffff0000);
		}
		
		hoursText.setText(Html.fromHtml(departments.get(position).getCurrentDay().toHtmlString()));

		return row;
	}
}