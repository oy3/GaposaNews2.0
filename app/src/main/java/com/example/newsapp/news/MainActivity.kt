package com.example.newsapp.news

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.newsapp.data.News
import com.example.newsapp.data.NewsApi
import com.example.newsapp.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: NewsAdapter
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    var backupList: List<News>? = null
    var searchList: ArrayList<News>? = arrayListOf()
//    var searchLists: List<News> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        recyclerView = findViewById(R.id.reposRecyclerView)

        adapter = NewsAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        refreshNews()
        mSwipeRefreshLayout.setOnRefreshListener { refreshNews() }
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.orange,
            R.color.green,
            R.color.blue
        )

        checkConnectivity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        val editext = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        editext.hint = "Search for news here..."
        mSwipeRefreshLayout.isRefreshing = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // create temporary list
                // loop through backup list
                // check any items that contain the query
                // if is contained add to temp list
                //after loop is finished update RV

                getFilter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    getFilter(newText)
                } else {
                    refreshNews()
                }

                return true
            }

        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

//    private fun searchView(){
//
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == R.id.menu_search) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun getFilter(charString: String) {
//        val newsLists: ArrayList<String> = arrayListOf
        searchList?.clear()
        if (backupList != null) {
            for (news in backupList!!) {
                if (news.title.toLowerCase().contains(charString.toLowerCase() as CharSequence, true)) {
                    searchList?.add(news)
                }
            }
        }
        adapter.updateNews(searchList!!)

    }


    private fun refreshNews() {
        val newsApi = NewsApi.create().getNews()
        newsApi.enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                if (response.body() != null) {
                    Collections.sort(response.body(), kotlin.Comparator<News> { t1, t2 ->
                        return@Comparator t2.id.compareTo(t1.id)
                    })
                    backupList = response.body()
                    adapter.updateNews(response.body()!!)
                }
//                txtError.text = ""
                txtError.visibility = View.GONE
                mSwipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<News>>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "Check network connection", Toast.LENGTH_SHORT).show()
                Snackbar.make(main_view, "Check network connection", Snackbar.LENGTH_LONG).show()
                txtError.setText("No news found")
//                txtError.setText(t.localizedMessage)
//                onSNACK(main_view)
                mSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun checkConnectivity() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (!isConnected) {
//            onSNACK(main_view)
            Snackbar.make(main_view, "Check network connection", Snackbar.LENGTH_LONG).show()
            txtError.setText("No news found")
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


    private fun onSNACK(view: View) {
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Check network connection", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 20f
        snackbar.show()
    }

    companion object {
        val TAG = MainActivity::class.java.name
    }
}
