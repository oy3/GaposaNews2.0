package com.example.newsapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface NewsApi {


    @GET("mysql_to_json.php")
    fun getNews(): Call<List<News>>


//    fun provideRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("http://gaposanews.com.ng/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    fun provideGitHubApi(): NewsApi {
//        return provideRetrofit().create(NewsApi::class.java)
//    }

    companion object {

        var BASE_URL = "http://news.gaposa.edu.ng/"

        fun create() : NewsApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(NewsApi::class.java)

        }
    }
}