package com.example.gaposanews

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.News
import com.example.newsapp.newsDetail.NewsDetailActivity
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.ctx
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_list.view.*
import timber.log.Timber


class NewsAdapter(
    private val context: Context
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var newsList: List<News> = listOf()
    var lastPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.activity_news_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNews(newsList: List<News>) {
        this.newsList = newsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])

        lastPosition = if (position > lastPosition) {
            //Scrolling down
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom)
            holder.itemView.startAnimation(animation)
            position
        } else {
            //Scrolling back up
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_top)
            holder.itemView.startAnimation(animation)
            position
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID, news)

            val p1 = android.support.v4.util.Pair(itemView.newsImg as View, "profile")
            val p2 = android.support.v4.util.Pair(itemView.newsTitle as View, "title")
            val p3 = android.support.v4.util.Pair(itemView.newsContent as View, "content")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as MainActivity, p1, p2, p3
            )
            context.startActivity(intent, options.toBundle())
        }

        private lateinit var news: News

        fun bind(news: News) {
            this.news = news
            itemView.newsTitle.text = news.title
            itemView.newsContent.text = news.content

            val imageUrl = "${BuildConfig.NEWS_DB_URL}${news.picture}"
            val titleM = news.title
            Timber.i("$titleM,Image url ======== $imageUrl")
            Picasso.with(itemView.context).load(imageUrl)
                .into(itemView.newsImg)


        }

    }


}