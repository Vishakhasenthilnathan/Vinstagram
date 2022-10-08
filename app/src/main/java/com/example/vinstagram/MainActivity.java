package com.example.vinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    TextView LoginTextButton;
    Boolean isSignupModeActive = true;
    EditText userNameText;
    EditText passwordText;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    public void onClick(View view){
        if(isUserNamePasswordPresent(view) &&view.getId() == R.id.LoginButton) {
            String username = userNameText.getText().toString();
            String password = passwordText.getText().toString();

            if(username.isEmpty()  || password.isEmpty() ){
                Toast.makeText(this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
            }
            else{
                Button SignupButton = findViewById(R.id.SignUpButton);
                if (isSignupModeActive) {
                    SignupButton.setText("LOGIN");
                    isSignupModeActive = false;
                    LoginTextButton.setText("SIGNUP");
                } else {
                    SignupButton.setText("SIGNUP");
                    isSignupModeActive = true;
                    LoginTextButton.setText("LOGIN");
                }
            }
        }
        else if(isUserNamePasswordPresent(view) &&view.getId() == R.id.LoginButton){
            Toast.makeText(this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.background_layout || view.getId() == R.id.logoimageView)
        {
//      InputMethodManager inputManager = (InputMethodManager)
//              this.getSystemService(this.INPUT_METHOD_SERVICE);
//      inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
//              InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public Boolean isUserNamePasswordPresent(View view){
        userNameText = findViewById(R.id.userNameET);
        passwordText = findViewById(R.id.passwordET);
        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.isEmpty()  || password.isEmpty() ){
            Toast.makeText(this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }
    public void SignupClicked(View view){
        if(isUserNamePasswordPresent(view)){
            if(isSignupModeActive) {
                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(userNameText.getText().toString());
                parseUser.setPassword(passwordText.getText().toString());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                ParseUser.logInInBackground(userNameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            showUserList();
                        }else {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginTextButton = findViewById(R.id.LoginButton);

        ImageView logo = findViewById(R.id.logoimageView);
        RelativeLayout background_layout = findViewById(R.id.background_layout);
        logo.setOnClickListener(this::onClick);
        background_layout.setOnClickListener(this::onClick);

        LoginTextButton.setOnClickListener(this::onClick);

        if(ParseUser.getCurrentUser()!=null){
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i== KeyEvent.KEYCODE_ENTER && keyEvent.getAction() ==KeyEvent.ACTION_DOWN){
            SignupClicked(view);
        }
        return false;
    }

}