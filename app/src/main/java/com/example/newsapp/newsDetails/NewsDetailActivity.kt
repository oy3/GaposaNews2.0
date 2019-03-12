package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {

    companion object {
       val EXTRA_NEWS_ID = "EXTRA_NEWS_ID"

        fun newIntent(context: Context, news: News): Intent {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(EXTRA_NEWS_ID, news)
            return intent
        }
    }

    private val news by lazy { intent.getSerializableExtra(EXTRA_NEWS_ID) as News }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        

        repoDetailName.text = news.title
        newsDate.text = news.dt
        repoDetailDescription.text = news.content
        newsPublisher.text = news.publisher
        Picasso.with(this).load("http://news.gaposa.edu.ng/image/${news.picture}").into(detailImg)
    }

}
