package com.example.myapplication01;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("/value")
    Call<DataModel> createPost(@Body DataModel dataModal);

}