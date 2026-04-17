package com.gsm.newscircle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gsm.newscircle.databinding.ActivityMainBinding
import com.gsm.newscircle.ui.country.NewsByCountryActivity
import com.gsm.newscircle.ui.language.NewsByLanguageActivity
import com.gsm.newscircle.ui.offline.OfflineTopHeadlineActivity
import com.gsm.newscircle.ui.pagination.TopHeadlinePaginationActivity
import com.gsm.newscircle.ui.search.SearchNewsActivity
import com.gsm.newscircle.ui.source.NewsSourceActivity
import com.gsm.newscircle.ui.topheadline.TopHeadlineActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {
            cardTopHeadlines.setOnClickListener {
                startActivity(Intent(this@MainActivity, TopHeadlineActivity::class.java))
            }
            cardTopHeadlinesOfflineArchitecture.setOnClickListener {
                startActivity(Intent(this@MainActivity, OfflineTopHeadlineActivity::class.java))
            }
            cardTopHeadlinesPagination.setOnClickListener {
                startActivity(Intent(this@MainActivity, TopHeadlinePaginationActivity::class.java))
            }
            cardNewsSources.setOnClickListener {
                startActivity(Intent(this@MainActivity, NewsSourceActivity::class.java))
            }
            cardCountrySelection.setOnClickListener {
                startActivity(Intent(this@MainActivity, NewsByCountryActivity::class.java))
            }
            cardLanguageSelection.setOnClickListener {
                startActivity(Intent(this@MainActivity, NewsByLanguageActivity::class.java))
            }
            cardSearch.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchNewsActivity::class.java))
            }
        }
    }
}