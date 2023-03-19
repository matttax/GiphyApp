package com.matttax.giphyvk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matttax.giphyvk.retrofit.GeneralData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GifsAdapter(val context: MainActivity, val gifs: MutableList<GeneralData>) : RecyclerView.Adapter<GifsAdapter.ViewHolder>() {

    lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.gifImage)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.gif_image_layout, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = gifs[position]
       CoroutineScope(Dispatchers.IO).launch {
            context.runOnUiThread {
                Glide.with(context).load(data.imageData.gifImage.url).into(holder.imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

}
