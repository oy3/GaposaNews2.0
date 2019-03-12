package com.example.newsapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_detail.view.*

class NewsDetailAdapter(val context: Context)
    : RecyclerView.Adapter<NewsDetailAdapter.ViewHolder>() {


    var newsList : List<News> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.activity_news_detail))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size

    fun updateRepos(news: List<News>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var news: News

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(news: News) {
            this.news = news
            itemView.repoDetailName.text = news.title
            itemView.newsDate.text = news.dt
            itemView.repoDetailDescription.text = news.content
            itemView.newsPublisher.text = news.publisher
           Picasso.with(itemView.context).load("http://gaposanews.com.ng/backend/image/${news.picture}").into(itemView.detailImg)
        }

        override fun onClick(view: View) {
            val context = view.context
            context.startActivity(NewsDetailActivity.newIntent(context, news))
        }
    }
}