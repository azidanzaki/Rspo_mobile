package com.example.rspo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login.php")
    fun login(@Body request: HashMap<String, String>): Call<LoginResponse>

    @POST("sign_up.php")
    fun signUp(@Body request: HashMap<String, String>): Call<LoginResponse>
}

