package com.example.newsapp.newsDetail

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.view.MenuItem
import android.widget.TextView
import com.example.newsapp.R
import com.example.newsapp.data.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat
import java.util.*


class NewsDetailActivity : AppCompatActivity() {

    companion object {
        val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"
    }

    private val news by lazy { intent.getSerializableExtra(EXTRA_NEWS_ID) as News }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val copyDescrText = findViewById<TextView>(R.id.newsDescr)
        copyDescrText.setTextIsSelectable(true)

        val copyTitleText = findViewById<TextView>(R.id.newsTitle)
        copyTitleText.setTextIsSelectable(true)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))


        newsTitle.text = news.title

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time = sdf.parse(news.dt).time
        val now = System.currentTimeMillis()

        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS)


        newsDate.text = "Posted $ago"
        newsDescr.text = news.content
        publisher.text = "By ${news.publisher}"
        Picasso.with(this).load("http://news.gaposa.edu.ng/image/${news.picture}").into(newsImg)
    }


}