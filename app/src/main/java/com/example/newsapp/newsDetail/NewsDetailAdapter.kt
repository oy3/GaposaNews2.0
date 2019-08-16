package com.example.newsapp.newsDetail

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import com.example.newsapp.ctx
import com.example.newsapp.data.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.view.*

class NewsDetailAdapter(private var newsList: List<News>) : RecyclerView.Adapter<NewsDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.activity_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size

    fun updateNews(news: List<News>) {
        this.newsList = news
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var news: News

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(news: News) {
            this.news = news
            itemView.newsTitle.text = news.title
            itemView.newsDate.text = news.dt
            itemView.newsDescr.text = news.content
            itemView.publisher.text = news.publisher
            Picasso.with(itemView.context).load("http://gaposanews.com.ng/backend/image/${news.picture}")
                .into(itemView.newsImg)
        }

        override fun onClick(view: View) {
            val context = view.context
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID, news)
            context.startActivity(intent)
        }
    }
}