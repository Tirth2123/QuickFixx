package com.example.quickfixx;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    //public static final String BASE_URL = "http://10.0.2.2:3000";
    public static final String BASE_URL = "https://quickfixxapi-production.up.railway.app";

    public static Api getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(Api.class);
    }
}
