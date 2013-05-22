/**
 * Authors: Chris, Tony, Gurpreet, Maria, Dustin,Dylan
 * Date: 5/22/13
 * Version:1.0
 * Description: This is a dialog containing info about the app and the giving child
 * 
 * History:
 *   original 5/22/13
 */
package csci307.theGivingChild.CleanWaterGame;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AboutDialog extends Activity {
	
	@Override
	public void onCreate(Bundle onSaveInstance)
	{
		super.onCreate(onSaveInstance);
		setContentView(R.layout.about_display);
		
		TextView t = (TextView)findViewById(R.id.aboutDisplay);
		t.setMovementMethod(new ScrollingMovementMethod());
	}

}
