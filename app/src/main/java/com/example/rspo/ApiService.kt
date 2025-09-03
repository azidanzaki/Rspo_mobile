package com.example.rspo

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("login.php")
    fun login(@Body request: HashMap<String, String>): Call<LoginResponse>

    @POST("sign_up.php")
    fun signUp(@Body request: HashMap<String, String>): Call<LoginResponse>

    @GET("get_pks.php")
    fun getPks(): Call<PksResponse>

    @GET("get_kebun.php")
    fun getKebun(): Call<KebunResponse>

    @GET("get_afdeling.php")
    fun getAfdeling(): Call<AfdelingResponse>

    @Multipart
    @POST("insert_pks.php")
    fun insertPks(
        @Part("tanggal") tanggal: RequestBody,
        @Part("nama_pks") namaPks: RequestBody,
        @Part("tujuan") tujuan: RequestBody,
        @Part("blanko") blanko: RequestBody,
        @Part("nopol") nopol: RequestBody,
        @Part("supir") supir: RequestBody,
        @Part foto1: MultipartBody.Part?,
        @Part foto2: MultipartBody.Part?,
        @Part foto3: MultipartBody.Part?
    ): Call<PksInputResponse>

    @Multipart
    @POST("insert_kebun.php")
    fun insertKebun(
        @Part("tanggal") tanggal: RequestBody,
        @Part("nama_kebun") namaKebun: RequestBody,
        @Part("afdeling") afdeling: RequestBody,
        @Part("blanko") blanko: RequestBody,
        @Part("nopol") nopol: RequestBody,
        @Part("supir") supir: RequestBody,
        @Part foto1: MultipartBody.Part?,
        @Part foto2: MultipartBody.Part?,
        @Part foto3: MultipartBody.Part?
    ): Call<KebunInputResponse>


}

