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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class BugReport extends Activity {

	public static final String ERROR_CODE_KEY = "csci370.theGivingChild.Error.boolerror";
	public static final String ERROR_CODE_NUM = "csci370.theGivingChild.Error.code";
	public static final String ERROR_CODE_DESC = "csci370.theGivingChild.Error.desc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getIntent().getExtras();
		
		if(b != null)
		{
			if (b.getBoolean(ERROR_CODE_KEY,false)) 
			{
				setContentView(R.layout.userbugreport);
				
			}
			else
			{
				setContentView(R.layout.systembugreport);
				TextView code = (TextView)findViewById(R.id.systemBug_errorCode);
				TextView descrpt = (TextView)findViewById(R.id.systemBug_errorDescription);
				Button ok = (Button)findViewById(R.id.systemBug_ok);

				code.setText(b.getString(ERROR_CODE_NUM));
				descrpt.setText(b.getString(ERROR_CODE_DESC));
			}
			
		}
		else
		{
			//error fun
		}
	}

	private OnClickListener userSend = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

	};

	private OnClickListener userCancel = new OnClickListener(){

		@Override
		public void onClick(View v) {
			finish();
		}

	};

	private OnClickListener sysOk = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

	};


}
