package com.example.newsapp.data

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class News(
    val id: Int,
    val title: String,
    val content: String,
    val picture: String,
    val dt: String,
    val publisher: String
) : Serializable {

    val date: Date get() = SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(dt)
}