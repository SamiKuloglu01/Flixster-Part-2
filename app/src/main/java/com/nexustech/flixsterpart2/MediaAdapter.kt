package com.nexustech.flixsterpart2

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class MediaAdapter(private val mediaItems: List<MediaItem>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = mediaItems[position]
        bindMediaItem(holder, mediaItem)
    }

    override fun getItemCount() = mediaItems.size

    private fun bindMediaItem(holder: MediaViewHolder, mediaItem: MediaItem) {
        holder.titleTextView.text = mediaItem.title
        loadPosterImage(holder, mediaItem.posterPath)
        setupItemClickListener(holder, mediaItem)
    }

    private fun loadPosterImage(holder: MediaViewHolder, posterPath: String?) {
        Glide.with(holder.itemView.context)
            .load(posterPath)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(holder.posterImageView)
    }

    private fun setupItemClickListener(holder: MediaViewHolder, mediaItem: MediaItem) {
        holder.itemView.setOnClickListener {
            // Ensure you cast context to Activity
            val activity = holder.itemView.context as Activity
            val intent = Intent(activity, DisplayPosterDetailsActivity::class.java).apply {
                putExtra("TITLE", mediaItem.title)
                putExtra("POSTER_PATH", mediaItem.posterPath)
                putExtra("POPULARITY", mediaItem.popularity)
                putExtra("VOTE_COUNT", mediaItem.voteCount)
                putExtra("OVERVIEW", mediaItem.overview)
            }

            // Create an options object for the shared element transition
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,                      // Pass the Activity context
                holder.posterImageView,        // The shared element
                "posterTransition"             // The transition name
            )

            // Start the detail activity with the transition options
            activity.startActivity(intent, options.toBundle())
        }
    }


    class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tv_media_title)
        val posterImageView: ImageView = view.findViewById(R.id.iv_media_poster)
    }
}
