package com.example.quickfixx;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
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


        LinearLayout signupButton = findViewById(R.id.Signup_Signup_Button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_name)).getText()).toString();
                String address = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Address)).getText()).toString();
                String city = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_City)).getText()).toString();
                String state = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_State)).getText()).toString();
                String phoneNo = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Phone_No)).getText()).toString();
                String emailId = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_EmailId)).getText()).toString();
                String password = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.Input2_Password)).getText()).toString();

                User user = new User(name, address, city, state, phoneNo, emailId, password);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Baseurl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api api = retrofit.create(Api.class);

                Call<ResponseBody> call = api.registerUser(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Response is successful", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Response is not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
