package com.example.quickfixx;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("login")
    Call<ResponseBody> loginUser(@Body User user);

    @POST("register")
    Call<ResponseBody> registerUser(@Body User user);
}

