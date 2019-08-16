package com.example.gaposanews.data

import com.example.newsapp.fcm.Token
import com.example.newsapp.data.News
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface NewsApi {
    @GET("mysql_to_json.php")
    fun getNews(): Call<List<News>>

    @POST("firebase/register.php")
    @FormUrlEncoded
    fun sendTokenToUrl(@Field("token") token: String): Call<Token>
}