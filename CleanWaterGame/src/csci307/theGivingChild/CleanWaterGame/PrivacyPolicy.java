/**
 * Authors: Chris, Tony, Gurpreet, Maria, Dustin,Dylan
 * Date: 5/22/13
 * Version:1.0
 * Description: This is a dialog containing the privacy policy
 * 
 * History:
 *   original 5/22/13
 */
package csci307.theGivingChild.CleanWaterGame;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class PrivacyPolicy extends Activity {

	@Override
	public void onCreate(Bundle onSaveInstance)
	{
		super.onCreate(onSaveInstance);
		setContentView(R.layout.privacy_policy);
		
		TextView t = (TextView)findViewById(R.id.privacyPolicy);
		t.setMovementMethod(new ScrollingMovementMethod());
	}
}
