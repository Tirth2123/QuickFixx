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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup_Page extends AppCompatActivity {
    //private String Baseurl ="http://192.168.234.42:3000";
    private String Baseurl ="http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emailId = sharedPreferences.getString("emailId", null);
        String password = sharedPreferences.getString("password", null);
        if (emailId != null && password != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView login = findViewById(R.id.SignUp_Login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Page.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout signupButton = findViewById(R.id.Signup_Signup_Button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputEditText nameInput = findViewById(R.id.Input2_name);
                TextInputEditText addressInput = findViewById(R.id.Input2_Address);
                TextInputEditText cityInput = findViewById(R.id.Input2_City);
                TextInputEditText stateInput = findViewById(R.id.Input2_State);
                TextInputEditText phoneNoInput = findViewById(R.id.Input2_Phone_No);
                TextInputEditText emailIdInput = findViewById(R.id.Input2_EmailId);
                TextInputEditText passwordInput = findViewById(R.id.Input2_Password);

                TextView signup_errorTextView_name = findViewById(R.id.signup_error_text_view_name);
                TextView signup_errorTextView_address = findViewById(R.id.signup_error_text_view_address);
                TextView signup_errorTextView_city = findViewById(R.id.signup_error_text_view_city);
                TextView signup_errorTextView_state = findViewById(R.id.signup_error_text_view_state);
                TextView signup_errorTextView_phone = findViewById(R.id.signup_error_text_view_phone);
                TextView signup_errorTextView_email = findViewById(R.id.signup_error_text_view_email);
                TextView signup_errorTextView_pass = findViewById(R.id.signup_error_text_view_pass);


                String name = Objects.requireNonNull(nameInput.getText()).toString();
                String address = Objects.requireNonNull(addressInput.getText()).toString();
                String city = Objects.requireNonNull(cityInput.getText()).toString();
                String state = Objects.requireNonNull(stateInput.getText()).toString();
                String phoneNo = Objects.requireNonNull(phoneNoInput.getText()).toString();
                String emailId = Objects.requireNonNull(emailIdInput.getText()).toString();
                String password = Objects.requireNonNull(passwordInput.getText()).toString();


                if (name.isEmpty()) {
                    signup_errorTextView_name.setText("Name cannot be empty");
                    signup_errorTextView_name.setVisibility(View.VISIBLE);
                    return;
                } else {
                    signup_errorTextView_name.setVisibility(View.GONE);
                }

                if (address.isEmpty()) {
                    signup_errorTextView_address.setText("Address cannot be empty");
                    signup_errorTextView_address.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_address.setVisibility(View.GONE);
                }

                if (city.isEmpty()) {
                    signup_errorTextView_city.setText("City cannot be empty");
                    signup_errorTextView_city.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_city.setVisibility(View.GONE);
                }

                if (state.isEmpty()) {
                    signup_errorTextView_state.setText("State cannot be empty");
                    signup_errorTextView_state.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_state.setVisibility(View.GONE);
                }

                if (phoneNo.isEmpty()) {
                    signup_errorTextView_phone.setText("Phone number cannot be empty");
                    signup_errorTextView_phone.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_phone.setVisibility(View.GONE);
                }

                if (phoneNo.length() < 10) {
                    signup_errorTextView_phone.setText("Enter valid Phone Number");
                    signup_errorTextView_phone.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_phone.setVisibility(View.GONE);
                }

                if (emailId.isEmpty()) {
                    signup_errorTextView_email.setText("Email cannot be empty");
                    signup_errorTextView_email.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_email.setVisibility(View.GONE);
                }

                Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
                Matcher emailMatcher = emailPattern.matcher(emailId);
                if (!emailMatcher.matches()) {
                    signup_errorTextView_email.setText("Invalid email format");
                    signup_errorTextView_email.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_email.setVisibility(View.GONE);
                }

                if (password.isEmpty()) {
                    signup_errorTextView_pass.setText("Password cannot be empty");
                    signup_errorTextView_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_pass.setVisibility(View.GONE);
                }

                if (password.length() < 8) {
                    signup_errorTextView_pass.setText("Password must be at least 8 characters long");
                    signup_errorTextView_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_pass.setVisibility(View.GONE);
                }

                Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
                Matcher passwordMatcher = passwordPattern.matcher(password);
                if (!passwordMatcher.matches()) {
                    signup_errorTextView_pass.setText("Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character");
                    signup_errorTextView_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    signup_errorTextView_pass.setVisibility(View.GONE);
                }

                User user = new User(name, address, city, state, phoneNo, emailId, password);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api api = retrofit.create(Api.class);

                Call<ResponseBody> call = api.registerUser(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("emailId", emailId);
                            myEdit.putString("password", password);
                            myEdit.commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            if (response.code() == 409) {
                                Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}