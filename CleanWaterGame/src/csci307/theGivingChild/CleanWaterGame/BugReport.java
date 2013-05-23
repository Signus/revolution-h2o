/**
* Authors: Maria, Chris, Tony, Gurpreet, Dylan, Dustin
* Date: 5/16/13
* Verson: 1.0
* Descripion: this activity will show up as a dilog and allow for error reporting
*
* Note: depending on the intent (system report v user report) you'll pass in a boolean
*   true for user report and no boolean for system
*   for system error you'll pass in the code and description to the intent
*/
package csci307.theGivingChild.CleanWaterGame;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class BugReport extends Activity {

	public static final String ERROR_CODE_KEY = "csci370.theGivingChild.Error.boolerror";
	public static final String ERROR_CODE_NUM = "csci370.theGivingChild.Error.code";
	public static final String ERROR_CODE_DESC = "csci370.theGivingChild.Error.desc";

	private Spinner related;
	private Button send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		
		if(b != null)
		{
			if (b.getBoolean(ERROR_CODE_KEY,false)) 
			{
				setContentView(R.layout.userbugreport);
				related = (Spinner)findViewById(R.id.userbug_related_list);
				ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
									R.array.userbug_relatedto_spinner,android.R.layout.simple_spinner_item);
				adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				related.setAdapter(adapt);
				send = (Button)findViewById(R.id.userbug_send);
				send.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						EditText sub = (EditText)findViewById(R.id.userbug_sub);
						String subject = sub.getText().toString()+":"+related.getSelectedItem().toString();
						Intent gmail = new Intent(Intent.ACTION_VIEW);
						gmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
						gmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"thegivingchild@gmail.com"});//set this to the feed back email site
						gmail.setData(Uri.parse("thegivingchild@gmail.com"));
						gmail.putExtra(Intent.EXTRA_SUBJECT, subject);
						gmail.setType("plain/text");
						startActivity(gmail);
						finish();
					}
					
				});
			}
			
		}
		else
		{
			//error fun
		}
	}

	private OnClickListener createUserSend()
	{
		OnClickListener userSend = new OnClickListener(){
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
	
		};

		return userSend;
	}

	private OnClickListener createUserCancel()
	{
		OnClickListener userCancel = new OnClickListener(){
	
			@Override
			public void onClick(View v) {
				finish();
			}
	
		};
		return userCancel;
	}

	private OnClickListener createSysOk()
	{
		OnClickListener sysOk = new OnClickListener(){
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
	
		};
		return sysOk;
	}
}
