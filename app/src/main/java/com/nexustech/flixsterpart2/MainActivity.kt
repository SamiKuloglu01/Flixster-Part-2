package com.nexustech.flixsterpart2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val apiKey = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
    private val movieUrl = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey"
    private val tvShowUrl = "https://api.themoviedb.org/3/tv/popular?api_key=$apiKey"
    private val baseUrl = "https://image.tmdb.org/t/p/w500"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchMovies()
        fetchTvShows()
    }

    private fun fetchMovies() {
        val client = OkHttpClient()
        val request = Request.Builder().url(movieUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val movies = parseMovies(jsonString)
                    runOnUiThread {
                        setupRecyclerView(R.id.rv_popular_movies, movies)
                    }
                }
            }
        })
    }

    private fun fetchTvShows() {
        val client = OkHttpClient()
        val request = Request.Builder().url(tvShowUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    Log.d("TvShowsResponse", jsonString)
                    val tvShows = parseTvShows(jsonString)
                    runOnUiThread {
                        setupRecyclerView(R.id.rv_popular_tv_shows, tvShows)
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(recyclerViewId: Int, mediaItems: List<MediaItem>) {
        findViewById<RecyclerView>(recyclerViewId).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MediaAdapter(mediaItems)
        }
    }

    private fun parseMovies(jsonString: String): List<MediaItem> {
        val movieResponse = Gson().fromJson(jsonString, MovieResponse::class.java)
        return movieResponse.results.map {
            MediaItem(
                title = it.title,
                posterPath = "${baseUrl}${it.poster_path}",
                popularity = it.popularity,
                voteCount = it.vote_count,
                overview = it.overview
            )
        }
    }

    private fun parseTvShows(jsonString: String): List<MediaItem> {
        val tvShowResponse = Gson().fromJson(jsonString, TvShowResponse::class.java)
        return tvShowResponse.results.map {
            MediaItem(
                title = it.name,
                posterPath = "${baseUrl}${it.poster_path}",
                popularity = it.popularity,
                voteCount = it.vote_count,
                overview = it.overview
            )
        }
    }
}
