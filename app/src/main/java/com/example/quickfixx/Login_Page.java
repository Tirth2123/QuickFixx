package com.example.quickfixx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Page extends AppCompatActivity {
    private String Baseurl ="http://192.168.234.42:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextView signup = findViewById(R.id.Login_SignUp);
        LinearLayout login = findViewById(R.id.Login_Login_Button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup_Page.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String identifier = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input1_Username)).getText()).toString();
                String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input1_Password)).getText()).toString();

// Create Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

// Create Api instance
                Api api = retrofit.create(Api.class);

// Call the API
                Call<ResponseBody> call = api.loginUser(new User(identifier, password));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Handle the response
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Handle the error
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle the error
                    }
                });
            }
        });
    }
}