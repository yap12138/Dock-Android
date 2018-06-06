package com.yaphets.dock.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yaphets.dock.R;
import com.yaphets.dock.model.UserInfo;
import com.yaphets.dock.model.validation.RegisterValidation;
import com.yaphets.dock.model.validation.Result;
import com.yaphets.dock.model.validation.Validator;

public class RegisterActivity extends AppCompatActivity {

    private EditText _email;
    private EditText _password;
    private EditText _nickname;

    private static final RegisterValidation REGISTER_VALIDATION = new RegisterValidation();

    private UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        _email = findViewById(R.id.edit_email);
        _password = findViewById(R.id.edit_password);
        _nickname = findViewById(R.id.edit_name);

        Button registerBtn = findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(v-> {
            attemptRegister();
        });
    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        String email = _email.getText().toString();
        String passwd = _password.getText().toString();
        String nickname = _nickname.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            _email.setError(getString(R.string.error_field_required));
            focusView = _email;
            cancel = true;
        } else if (TextUtils.isEmpty(passwd)) {
            _password.setError(getString(R.string.error_field_required));
            focusView = _password;
            cancel = true;
        } else if (TextUtils.isEmpty(nickname)) {
            _nickname.setError(getString(R.string.error_field_required));
            focusView = _nickname;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            _email.setError(getString(R.string.error_incorrect_email));
            focusView = _email;
            cancel = true;
        } else if (!Validator.isPasswordValid(passwd)) {
            _password.setError(getString(R.string.error_incorrect_password));
            focusView = _password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = new UserRegisterTask(email, passwd, nickname);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Result> {
        private final String mEmail;
        private final String mPasswd;
        private final String mNickname;

        UserRegisterTask(String email, String passwd, String nickname) {
            mEmail = email;
            mPasswd = passwd;
            mNickname = nickname;
        }

        @Override
        protected Result doInBackground(Void... voids) {
            UserInfo info = new UserInfo(mEmail, mPasswd, mNickname);

            return REGISTER_VALIDATION.isValid(info);
        }

        @Override
        protected void onPostExecute(final Result result) {
            mAuthTask = null;

            if (result.getCode() == 1) {
                Toast.makeText(RegisterActivity.this, R.string.success_sign_up, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Error: " + result.getCode() + "\nMessage: " + result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
