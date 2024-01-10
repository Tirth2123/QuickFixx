package com.example.quickfixx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    TextInputEditText emailInput, newPasswordInput, otpInput;
    TextView sendOtpButton, resendOtpTimerTextView, resendOtpTextView;
    TextView forgot_error_text_view_email, forgot_error_text_view_pass, forgot_error_text_view_otp;
    LinearLayout verifyOtpButton, otp_layout, forgot_layout;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        api = RetrofitClient.getClient();

        emailInput = findViewById(R.id.Forgot_Input_Username);
        newPasswordInput = findViewById(R.id.Forgot_Input_Password);
        otpInput = findViewById(R.id.enter_Otp);

        sendOtpButton = findViewById(R.id.forgot_button);
        resendOtpTimerTextView = findViewById(R.id.timer_text_view);
        resendOtpTextView = findViewById(R.id.resend_otp_text_view);

        verifyOtpButton = findViewById(R.id.verify_otp_layout);
        otp_layout = findViewById(R.id.Otp_Layout);
        forgot_layout = findViewById(R.id.forgot_layout);

        forgot_error_text_view_email = findViewById(R.id.forgot_error_text_view_email);
        forgot_error_text_view_pass = findViewById(R.id.forgot_error_text_view_pass);
        forgot_error_text_view_otp = findViewById(R.id.forgot_error_text_view_otp);

        verifyOtpButton = findViewById(R.id.verify_otp_layout);
        otp_layout = findViewById(R.id.Otp_Layout);
        forgot_layout = findViewById(R.id.forgot_layout);


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
                send_Otp();
            }
        });

        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_Otp();
                resendOtpTextView.setVisibility(View.GONE);

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
                            String serverResponse;
                            if (response.isSuccessful()) {
                                serverResponse = response.body().string();
                                Intent intent = new Intent(getApplicationContext(), Login_Page.class);
                                startActivity(intent);
                                finish();
                            } else {
                                serverResponse = response.errorBody().string();
                            }
                            Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();
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

    private void send_Otp() {
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
                        new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                resendOtpTimerTextView.setText(millisUntilFinished / 1000 + " Seconds Remaining");
                            }

                            public void onFinish() {
                                resendOtpTimerTextView.setText("Didnt Recived OTP??");
                                resendOtpTextView.setClickable(true);
                                resendOtpTextView.setVisibility(View.VISIBLE);
                            }
                        }.start();
                        resendOtpTextView.setClickable(false);
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
}