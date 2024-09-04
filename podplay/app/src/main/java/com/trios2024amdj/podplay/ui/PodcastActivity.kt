package com.trios2024amdj.podplay.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trios2024amdj.podplay.R
import com.trios2024amdj.podplay.databinding.ActivityPodcastBinding
import com.trios2024amdj.podplay.repository.ItunesRepo
import com.trios2024amdj.podplay.service.ItunesService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPodcastBinding

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itunesService = ItunesService.instance

        val itunesRepo = ItunesRepo(itunesService)

        GlobalScope.launch {
            val results = itunesRepo.searchByTerm("Android Developer")
            Log.i(TAG, "Results = ${results.body()}")
        }

        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // 1
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        // 2
        val searchMenuItem = menu.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView as SearchView
        // 3
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // 4
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    private fun performSearch(term: String) {
        val itunesService = ItunesService.instance
        val itunesRepo = ItunesRepo(itunesService)

        GlobalScope.launch {
            val results = itunesRepo.searchByTerm(term)
            Log.i(TAG, "Results = ${results.body()}")
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY) ?: return
            performSearch(query)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

}