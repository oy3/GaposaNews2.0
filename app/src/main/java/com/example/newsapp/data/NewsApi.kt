package com.example.newsapp.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

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
            val interceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger {
                    Log.e("Network Request", it)
                }
            )
            val client = OkHttpClient.Builder()
            client.apply {
                addInterceptor(interceptor)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
            }
            val retrofit = Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(NewsApi::class.java)

        }
    }
}