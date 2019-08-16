package com.example.newsapp

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.gaposanews.NewsAdapter
import com.example.newsapp.data.NetworkService
import com.example.newsapp.data.News
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    lateinit var adapter: NewsAdapter
    private val networkService = NetworkService()
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    var backupList: List<News>? = null
    private var searchList: ArrayList<News>? = arrayListOf()

    private val callback = object : Callback<List<News>> {
        override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
            if (response.body() != null) {
                Collections.sort(response.body(), kotlin.Comparator<News> { t1, t2 ->
                    return@Comparator t2.id.compareTo(t1.id)
                })

                Handler().postDelayed({
                    progressLoading.visibility = View.GONE
                    val newsList = response.body()
                    backupList = response.body()
                    adapter.updateNews(newsList!!)
                }, 5000)

            }
        }

        override fun onFailure(call: Call<List<News>>, t: Throwable) {
            Timber.e(t, "Problem calling Movies API")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("onCreate called.")



        mSwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        adapter = NewsAdapter(this)
        recyclerView = findViewById(R.id.recyclerView)

        checkConnectivity()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        mSwipeRefreshLayout.setOnRefreshListener {
            checkConnectivity()
        }
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.orange,
            R.color.green,
            R.color.blue
        )

    }


    private fun checkConnectivity() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (!isConnected) {
            if (recyclerView.adapter != null && adapter.itemCount != 0) {
                txtError.visibility = View.GONE
                Snackbar.make(main_view, "Check network connection", Snackbar.LENGTH_LONG)
                    .setAction("RETRY") {
                        checkConnectivity()
                    }
                    .setActionTextColor(Color.RED)
                    .show()
                mSwipeRefreshLayout.isRefreshing = false
            } else if (adapter.itemCount == 0) {
                mSwipeRefreshLayout.isRefreshing = false
                errorView.visibility = View.VISIBLE
                Handler().postDelayed({

                    Snackbar.make(main_view, "Check network connection", Snackbar.LENGTH_LONG)
                        .setAction("RETRY") {
                            checkConnectivity()
                        }
                        .setActionTextColor(Color.RED)
                        .show()

                }, 5000)


            }
            progressLoading.visibility = View.GONE
        } else {
            errorView.visibility = View.GONE
            if (adapter.itemCount == 0) {
                progressLoading.visibility = View.VISIBLE
            } else {
                Snackbar.make(main_view, "Please wait while we refresh your feed...", Snackbar.LENGTH_LONG)
                    .show()
            }
            Handler().postDelayed({
                networkService.getNewsFromApi(callback)
                mSwipeRefreshLayout.isRefreshing = false
            }, 3000)

        }
    }


    private fun getNews() {
        networkService.getNewsFromApi(callback)
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
                getFilter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    getFilter(newText)
                } else {
                    getNews()
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun getFilter(charString: String) {
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

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.tap_back_to_exit), Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume called.")

    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause called.")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart called.")

    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart called.")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop called.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy called.")
    }

}

