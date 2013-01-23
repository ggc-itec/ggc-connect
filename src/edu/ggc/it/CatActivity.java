package edu.ggc.it;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CatActivity extends Activity {

	private Button catButton;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cat);
		context = this;
		catButton = (Button) findViewById(R.id.catButton);
		catButton.setOnClickListener(new CatListener());
	}
	
	public class CatListener implements OnClickListener {
		public void onClick(View view) {
			if( view.getId() == R.id.catButton)
			{
				Toast.makeText(context, "What a cute cat!", 
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
