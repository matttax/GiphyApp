package com.matttax.giphyapp.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.matttax.giphyapp.R
import com.matttax.giphyapp.model.GifModel

class GifsAdapter(
    private val context: Context,
    private val gifs: MutableList<GifModel>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<GifsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.gif_image_layout, parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = gifs[position]
        Glide.with(context)
            .load(data.url)
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.isVisible = false
                        return false
                    }

                }
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

    class ViewHolder(itemView: View, listener: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<ImageView?>(R.id.gifImage).apply {
            setOnClickListener { listener(adapterPosition) }
        }
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

}
