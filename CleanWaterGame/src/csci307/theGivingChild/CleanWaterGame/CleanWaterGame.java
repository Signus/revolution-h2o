package csci307.theGivingChild.CleanWaterGame;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
    formKey="",//depreciated not used but still required
    mailTo="ccard@mymail.mines.edu",
    customReportContent={ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.STACK_TRACE},
    mode=ReportingInteractionMode.TOAST,
    resToastText=40
)
public class CleanWaterGame extends Application {
	private static CleanWaterGame instance;
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		ACRA.init(this);
	}
	
	public static CleanWaterGame getInstance()
	{
		return instance;
	}

}
