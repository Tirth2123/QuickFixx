package com.example.quickfixx;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @POST("api/user/login")
    Call<ResponseBody> loginUser(@Body User user);

    @POST("api/user/register")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("api/user/send_otp")
    Call<ResponseBody> sendOtp(@Body HashMap<String, String> body);

    @POST("api/user/verify_otp")
    Call<ResponseBody> verifyOtp(@Body HashMap<String, String> body);

    @GET("api/profile/profiles")
    Call<List<Profile>> getProfiles();

    @POST("api/review/submit_review")
    Call<ResponseBody> submitReview(@Body Review review);

    @GET("api/review/reviews/{serviceProviderContact}")
    Call<List<Review>> getReviews(@Path("serviceProviderContact") String serviceProviderContact);
}
