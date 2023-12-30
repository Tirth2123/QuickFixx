package com.example.quickfixx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forgot_Password extends AppCompatActivity {

    private String Baseurl ="http://192.168.234.42:3000";
    //private String Baseurl ="http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        TextInputEditText emailInput = findViewById(R.id.Forgot_Input_Username);
        TextInputEditText newPasswordInput = findViewById(R.id.Forgot_Input_Password);
        TextInputEditText otpInput = findViewById(R.id.enter_Otp);
        TextView sendOtpButton = findViewById(R.id.forgot_button);
        LinearLayout verifyOtpButton = findViewById(R.id.verify_otp_layout);
        LinearLayout otp_layout = findViewById(R.id.Otp_Layout);
        LinearLayout forgot_layout = findViewById(R.id.forgot_layout);

        TextView forgot_error_text_view_email = findViewById(R.id.forgot_error_text_view_email);
        TextView forgot_error_text_view_pass = findViewById(R.id.forgot_error_text_view_pass);
        TextView forgot_error_text_view_otp = findViewById(R.id.forgot_error_text_view_otp);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Objects.requireNonNull(emailInput.getText()).toString();
                String newpass = Objects.requireNonNull(newPasswordInput.getText()).toString();

                if (email.isEmpty()) {
                    forgot_error_text_view_email.setText("Email id cannot be empty");
                    forgot_error_text_view_email.setVisibility(View.VISIBLE);
                    return;
                } else {
                    forgot_error_text_view_email.setVisibility(View.GONE);
                }

                if (newpass.isEmpty()) {
                    forgot_error_text_view_pass.setText("Password cannot be empty");
                    forgot_error_text_view_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    forgot_error_text_view_pass.setVisibility(View.GONE);
                }

                if (newpass.length() < 8) {
                    forgot_error_text_view_pass.setText("Password must be at least 8 characters long");
                    forgot_error_text_view_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    forgot_error_text_view_pass.setVisibility(View.GONE);
                }

                Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
                Matcher passwordMatcher = passwordPattern.matcher(newpass);
                if (!passwordMatcher.matches()) {
                    forgot_error_text_view_pass.setText("Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character");
                    forgot_error_text_view_pass.setVisibility(View.VISIBLE);
                    return;
                }else {
                    forgot_error_text_view_pass.setVisibility(View.GONE);
                }

                String emailId = Objects.requireNonNull(emailInput.getText()).toString();

                HashMap<String, String> body = new HashMap<>();
                body.put("emailId", emailId);

                Call<ResponseBody> call = api.sendOtp(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                String serverResponse = response.body().string();
                                Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();
                                otp_layout.setVisibility(View.VISIBLE);
                                forgot_layout.setVisibility(View.GONE);
                            } else {
                                String serverResponse = response.errorBody().string();
                                Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailId = Objects.requireNonNull(emailInput.getText()).toString();
                String otp = Objects.requireNonNull(otpInput.getText()).toString();
                String newPassword = Objects.requireNonNull(newPasswordInput.getText()).toString();

                if (otp.isEmpty()) {
                    forgot_error_text_view_otp.setText("Otp cannot be empty");
                    forgot_error_text_view_otp.setVisibility(View.VISIBLE);
                    return;
                }else {
                    forgot_error_text_view_otp.setVisibility(View.GONE);
                }

                if (otp.length() < 6) {
                    forgot_error_text_view_otp.setText("Enter Valid Otp");
                    forgot_error_text_view_otp.setVisibility(View.VISIBLE);
                    return;
                }else {
                    forgot_error_text_view_otp.setVisibility(View.GONE);
                }

                HashMap<String, String> body = new HashMap<>();
                body.put("emailId", emailId);
                body.put("otp", otp);
                body.put("newPassword", newPassword);

                Call<ResponseBody> call = api.verifyOtp(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                String serverResponse = response.body().string();
                                Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();
                            } else {
                                String serverResponse = response.errorBody().string();
                                Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
