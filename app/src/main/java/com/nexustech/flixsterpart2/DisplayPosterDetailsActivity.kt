package com.nexustech.flixsterpart2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DisplayPosterDetailsActivity : AppCompatActivity() {
    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var popularityTextView: TextView
    private lateinit var voteCountTextView: TextView
    private lateinit var overviewTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_poster_details)
        initViews()
        populateDataFromIntent()
    }

    private fun initViews() {
        posterImageView = findViewById(R.id.iv_movie_poster)
        posterImageView.transitionName = "posterTransition"
        titleTextView = findViewById(R.id.tv_title)
        popularityTextView = findViewById(R.id.tv_popularity)
        voteCountTextView = findViewById(R.id.tv_vote_count)
        overviewTextView = findViewById(R.id.tv_overview)
    }

    @SuppressLint("SetTextI18n")
    private fun populateDataFromIntent() {
        val title = intent.getStringExtra("TITLE") ?: "No Title"
        val posterPath = intent.getStringExtra("POSTER_PATH") ?: ""
        val popularity = intent.getFloatExtra("POPULARITY", 0f)
        val voteCount = intent.getIntExtra("VOTE_COUNT", 0)
        val overview = intent.getStringExtra("OVERVIEW") ?: "No Overview"

        titleTextView.text = title
        popularityTextView.text = "Popularity: $popularity"
        voteCountTextView.text = "Vote Count: $voteCount"
        overviewTextView.text = overview

        if (posterPath.isNotEmpty()) {
            Glide.with(this)
                .load(posterPath)
                .into(posterImageView)
        } else {
            posterImageView.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}
