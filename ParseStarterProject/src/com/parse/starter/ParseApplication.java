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
		Parse.initialize(this, "Y6wEKCeBbSfOTP0zHcOW3E6vsHa8LbZiWvYaxtQ5", "SMCAoDyLicA3m8R7ZSfnXvlAnw9A2aJlMjo6BwP5");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		defaultACL.setPublicReadAccess(true);
//        defaultACL.setPublicWriteAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
