package com.example.quickfixx;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup_Page extends AppCompatActivity {
    private String Baseurl ="http://192.168.234.42:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        TextView login = findViewById(R.id.SignUp_Login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Page.class);
                startActivity(intent);
            }
        });

        // Get the signup button
        LinearLayout signupButton = findViewById(R.id.Signup_Signup_Button);

        // Set the click listener for the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input values
                String name = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_name)).getText()).toString();
                String address = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Address)).getText()).toString();
                String city = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_City)).getText()).toString();
                String state = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_State)).getText()).toString();
                String phoneNo = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Phone_No)).getText()).toString();
                String emailId = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_EmailId)).getText()).toString();
                String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Password)).getText()).toString();

                // Create a new User object
                User user = new User(name, address, city, state, phoneNo, emailId, password);
                // Add other fields as necessary

                // Create Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create Api instance
                Api api = retrofit.create(Api.class);

                // Call the API
                Call<ResponseBody> call = api.registerUser(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Handle the response
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            // Display a success message
                            Toast.makeText(getApplicationContext(), "Response is successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the error
                            // Display an error message
                            Toast.makeText(getApplicationContext(), "Response is not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (t instanceof IOException) {
                            // Network error
                            Toast.makeText(getApplicationContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Network error: ", t);
                        } else if (t instanceof HttpException) {
                            // HTTP error
                            HttpException httpException = (HttpException) t;
                            int statusCode = httpException.code();
                            if (statusCode == 400) {
                                // Invalid request
                                Toast.makeText(getApplicationContext(), "Invalid data entered. Please check your details.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other HTTP error
                                Toast.makeText(getApplicationContext(), "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "HTTP error: ", t);
                        } else {
                            // Other error
                            Toast.makeText(getApplicationContext(), "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Unknown error: ", t);
                        }
                    }
                });
            }
        });
    }
}
