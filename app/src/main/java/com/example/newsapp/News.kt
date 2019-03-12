package com.example.newsapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class News(
    val id:Int,
    val title: String,
    val content: String,
    val dt: String,
    val publisher: String,
    @SerializedName("picture") val picture: String): Serializable
