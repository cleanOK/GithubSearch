package com.dmytrod.githubsearch.screens.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.dmytrod.githubsearch.App
import com.dmytrod.githubsearch.R
import com.dmytrod.githubsearch.model.Repository
import com.dmytrod.githubsearch.repositories.AuthRepositoryImpl
import com.dmytrod.githubsearch.util.ThrottleTrackingBus
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


class SearchActivity : AppCompatActivity(), SearchContract.View, SearchView.OnQueryTextListener {

    val LOG_TAG = "SearchActivity"

    private val mRepositories = mutableListOf<Repository>()
    @Inject
    lateinit var mPresenter: SearchContract.Presenter

    private lateinit var mSearchQuery: String
    private lateinit var mHandler: Handler
    private lateinit var mTrackingBus: ThrottleTrackingBus

    private var mIsLoading = false
    private var mIsLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDI()
        mPresenter.setView(this)
        initUI()
        if (intent != null && intent.data != null) {
            //Activity has been returned to after sign in
            mPresenter.processIntentData(intent.data)
        } else {
            mPresenter.checkAuthorization()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu!!.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem) = true

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Do something when collapsed
//                mImDbAdapter.setSearchResult(mListImDb)
                return true // Return true to collapse action view

            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        mTrackingBus = ThrottleTrackingBus({ onTrackViewResponse(it) }, {
            //TODO log
        })
    }

    override fun onPause() {
        mTrackingBus.unsubscribe()
        super.onPause()
    }

    private fun onTrackViewResponse(visibleState: ThrottleTrackingBus.VisibleState?) {
        //TODO
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        mSearchQuery = newText
        mHandler.removeCallbacksAndMessages(null)

        mHandler.postDelayed({
            mRepositories.clear()
            mPresenter.performSearch(mSearchQuery)
        }, 300)
        return true
    }

    override fun showResults(results: List<Repository>) {
        mRepositories.addAll(results)
        recycler.adapter.notifyDataSetChanged()
    }

    override fun showLoggedIn(result: Boolean) {
        Snackbar.make(root, "logged in = $result", Snackbar.LENGTH_SHORT).show()
    }

    override fun showError() {
        Snackbar.make(root, "login failed due to error", Snackbar.LENGTH_SHORT).show()
    }

    private fun initDI() {
        DaggerSearchComponent.builder()
                .applicationComponent((application as App).applicationComponent)
                .build()
                .inject(this)
    }

    private fun initUI() {
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        mHandler = Handler(Looper.getMainLooper())
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = RepositoryAdapter(mRepositories)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler.addOnScrollListener(PaginationListener(recycler.layoutManager as LinearLayoutManager))
        recycler.addOnScrollListener(SeenListener(recycler.layoutManager as LinearLayoutManager))
    }

    private inner class SeenListener(private val mLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleStateFinal = ThrottleTrackingBus.VisibleState(mLayoutManager.findFirstCompletelyVisibleItemPosition(), mLayoutManager.findLastCompletelyVisibleItemPosition())
            mTrackingBus.postViewEvent(visibleStateFinal)
        }
    }

    private fun navigateToAuth() {
        //TODO break into reusable pieces
        val uri = Uri.parse("https://github.com/login/oauth/authorize?client_id=${AuthRepositoryImpl.CLIENT_ID}&state=${AuthRepositoryImpl.STATE_VALUE}&allow_signup=false");
        val browserIntent = Intent(Intent.ACTION_VIEW)
                .setData(uri)
        startActivity(browserIntent)
    }

    private inner class PaginationListener internal constructor(private val mLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = mLayoutManager.childCount
            val totalItemCount = mLayoutManager.itemCount
            val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
            if (!mIsLoading && !mIsLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= SearchContract.PAGE_SIZE) {
                    loadNextPage()
                }
            }
        }
    }

    private fun loadNextPage() {
        mIsLoading = true
        //paging starts from 1
        val currentPage = mRepositories.size / SearchContract.PAGE_SIZE + 1
        mPresenter.loadPage(currentPage + 1)

    }

}
