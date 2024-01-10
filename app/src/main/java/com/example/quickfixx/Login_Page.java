package com.example.quickfixx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emailId = sharedPreferences.getString("emailId", null);
        String password = sharedPreferences.getString("password", null);
        if (emailId != null && password != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView signup = findViewById(R.id.Login_SignUp);
        TextView forgot = findViewById(R.id.login_forgot_password);
        TextView login_errorTextView_pass = findViewById(R.id.login_error_text_view_pass);
        TextView login_errorTextView_email = findViewById(R.id.login_error_text_view_email);

        LinearLayout login = findViewById(R.id.Login_Login_Button);

        TextInputEditText usernameInput = findViewById(R.id.Input1_Username);
        TextInputEditText passwordInput = findViewById(R.id.Input1_Password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup_Page.class);
                startActivity(intent);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = Objects.requireNonNull(usernameInput.getText()).toString();
                String password = Objects.requireNonNull(passwordInput.getText()).toString();

                if (Username.isEmpty()) {
                    login_errorTextView_email.setText("Email id cannot be empty");
                    login_errorTextView_email.setVisibility(View.VISIBLE);
                    return;
                } else {
                    login_errorTextView_email.setVisibility(View.GONE);
                }

                if (password.isEmpty()) {
                    login_errorTextView_pass.setText("Password cannot be empty");
                    login_errorTextView_pass.setVisibility(View.VISIBLE);
                    return;
                } else {
                    login_errorTextView_pass.setVisibility(View.GONE);
                }

                Api api = RetrofitClient.getClient();

                Call<ResponseBody> call = api.loginUser(new User(Username, password));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("emailId", Username);
                            myEdit.putString("password", password);
                            myEdit.commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.code() == 400) {
                                try {
                                    String errorMessage = response.errorBody().string();
                                    if (errorMessage.contains("User not found")) {
                                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                                    } else if (errorMessage.contains("Invalid password")) {
                                        Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
