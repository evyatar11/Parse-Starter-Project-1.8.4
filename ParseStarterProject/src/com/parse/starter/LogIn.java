package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends SetupUiKeyboard implements
        android.view.View.OnClickListener {

    TextView title, banner;
    Button login, signup;
    EditText mail, pass;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isGPSEnable();
        // link xml to code
        title = (TextView) findViewById(R.id.title);
        banner = (TextView) findViewById(R.id.facebook_banner);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        mail = (EditText) findViewById(R.id.etMail);
        pass = (EditText) findViewById(R.id.etPass);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        // set fonts
        initTypeface();
        setupUI(findViewById(R.id.parent));
        // track activity by parse.com
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    // set the fonts to the text vievs
    private void initTypeface() {
        Typeface pacifico = Typeface.createFromAsset(getAssets(),
                "pacifico.ttf");
        title.setTypeface(pacifico);
        banner.setTypeface(pacifico);
    }

    public void isGPSEnable() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {

            // login to parse database

            ParseUser.logInInBackground(mail.getText().toString(), pass
                    .getText().toString(), new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Intent intent = new Intent(LogIn.this, MainScreen.class);
                        intent.putExtra("user", mail.getText().toString());
                        String str = mail.getText().toString();
                        startActivity(intent);
                        finish();
                    } else {
                        // Signup failed. Look at the ParseException to
                        // see what happened.
                    }
                }
            });

        } else if (v.getId() == R.id.signup) {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        }

    }

}
