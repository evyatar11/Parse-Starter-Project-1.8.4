package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize Crash Reporting.
		ParseCrashReporting.enable(this);

		// Enable Local Datastore.
//		Parse.enableLocalDatastore(this);

		// Add your initialization code here
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Tab.class);
		ParseObject.registerSubclass(Post.class);
		Parse.initialize(this, "6yME94t22PAckT61vJXKbC5HtcRFqenDVBMLboD9", "cA242AT2n20dzyueOqL5p5nYXTceiaHKEMJvruwJ");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
