package com.example.newsapp.news

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.data.News
import com.example.newsapp.NewsDetailActivity
import com.example.newsapp.R
import com.example.newsapp.data.inflate
import kotlinx.android.synthetic.main.list_item_repo.view.*

class NewsAdapter(val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    //    var news: MutableList<News>
    var newsList: List<News> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_repo))

    }

    override fun getItemCount() = newsList.size

    fun updateNews(newsList: List<News>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val news = newsList.get(position)
//        holder.bind(news)
        holder.bind(newsList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var news: News

        fun bind(news: News) {
            this.news = news
            itemView.newsTitle.text = news.title
            itemView.newsDescription.text = news.content

            itemView.setOnClickListener {
                itemView.context.startActivity(
                    NewsDetailActivity.newIntent(
                        itemView.context,
                        news
                    )
                )
            }
        }
    }
}