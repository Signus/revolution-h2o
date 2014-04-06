/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/16/13
* Version: 1.0
* Description: this activity will show up as a dialog and allows the user to send feedback via e-mail
*
* History:
*  5/16/13 original 1.0
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

public class FeedBack extends Activity {
	
	private Spinner related;
	private Button send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
			
		setContentView(R.layout.userbugreport);
		
		//This populates the relates spinner and allows to get the users choice
		related = (Spinner)findViewById(R.id.userbug_related_list);
		ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
							R.array.userbug_relatedto_spinner,android.R.layout.simple_spinner_item);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		related.setAdapter(adapt);
		
		send = (Button)findViewById(R.id.userbug_send);
		send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String subject = related.getSelectedItem().toString()+":";
				//This sets up an email and uses the user email, the to line and the subject
				//to facilitate a easier feed back experience when they hit the compose button
				Intent gmail = new Intent(Intent.ACTION_SEND);
				gmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"thegivingchild@gmail.com"});//set this to the feed back email sit
				gmail.putExtra(Intent.EXTRA_SUBJECT, subject);
				gmail.setType("message/rfc822");
				startActivity(gmail);
				finish();
			}
			
		});
	}
}
