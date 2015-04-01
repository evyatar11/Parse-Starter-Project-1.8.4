package com.parse.starter;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends SetupUiKeyboard implements OnClickListener {
	private TextView signTitle;
	private Button createUser;
	private EditText signMail, signPass, signPhone, signNickname;
    private ParseUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		signTitle = (TextView) findViewById(R.id.signTitle);
		createUser = (Button) findViewById(R.id.createUser);
		signMail = (EditText) findViewById(R.id.signMail);
		signPass = (EditText) findViewById(R.id.signPass);
		signPhone = (EditText) findViewById(R.id.signPhone);
		signNickname = (EditText) findViewById(R.id.signNickname);
		createUser.setOnClickListener(this);
		initTypeface();
        setupUI(findViewById(R.id.parent));
	}

	private void initTypeface() {
		Typeface pacifico = Typeface.createFromAsset(getAssets(),
				"pacifico.ttf");
		signTitle.setTypeface(pacifico);
	}

	@Override
	public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.createUser) {
            // create new user
            user = new ParseUser();
            user.setUsername(signMail.getText().toString());
            user.setPassword(signPass.getText().toString());
            user.setEmail(signMail.getText().toString());
            // other fields can be set just like with ParseObject
            user.put("phone", signPhone.getText().toString());
            user.put("nickname", signNickname.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Show a simple Toast message upon successful registration
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });
        }
    }
}
