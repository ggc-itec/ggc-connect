package edu.ggc.it.d2l_web;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


/*
 *   Login form for D2L    
 */
public class D2LWebLoginActivity extends Activity
{

    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox saveCheckBox;
    private final String D2L_LOGIN_CHECKBOX_CHECKED = "CHECKBOX_CHECKED";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2l_web_login);
        usernameEditText = (EditText) findViewById(R.id.edittext_d2l_username);
        passwordEditText = (EditText) findViewById(R.id.edittext_d2l_password);
        loginButton = (Button) findViewById(R.id.button_d2l_login);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), D2LWebActivity.class);
                intent.putExtra(D2LWebActivity.D2L_LOGIN_NAME, usernameEditText.getText().toString());
                intent.putExtra(D2LWebActivity.D2L_PASSWORD_NAME, passwordEditText.getText().toString());
                startActivity(intent);
            }
        });

        saveCheckBox = (CheckBox) findViewById(R.id.checkbox_save_d2l_login);
        saveCheckBox.setOnCheckedChangeListener(new CheckBoxListener());

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        usernameEditText.setText(settings.getString(D2LWebActivity.D2L_LOGIN_NAME, ""));
        passwordEditText.setText(settings.getString(D2LWebActivity.D2L_PASSWORD_NAME, ""));
        saveCheckBox.setChecked(settings.getBoolean(D2L_LOGIN_CHECKBOX_CHECKED, false));
    }
    
    public class CheckBoxListener implements CompoundButton.OnCheckedChangeListener 
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
        {
            //FIXME: not best practice to store login info in sharedpreferences
            //login info could be compromised if the device is stolen but other apps can't see it 
            SharedPreferences settings = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            if (isChecked) 
            {
                editor.putString(D2LWebActivity.D2L_LOGIN_NAME, usernameEditText.getText().toString() );
                editor.putString(D2LWebActivity.D2L_PASSWORD_NAME, passwordEditText.getText().toString() );
                editor.putBoolean(D2L_LOGIN_CHECKBOX_CHECKED, saveCheckBox.isChecked());
                editor.commit();
            } 
            else 
            {
                editor.putString(D2LWebActivity.D2L_LOGIN_NAME, "" );
                editor.putString(D2LWebActivity.D2L_PASSWORD_NAME, "" );
                editor.putBoolean(D2L_LOGIN_CHECKBOX_CHECKED, saveCheckBox.isChecked());
                editor.commit();
            }
        }

    }

}
