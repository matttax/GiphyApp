package com.matttax.giphyvk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.matttax.giphyvk.databinding.ActivityGifBinding
import com.matttax.giphyvk.room.GifEntity
import com.matttax.giphyvk.room.MainDB

class GifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = MainDB.getDB(this)


        val titleText = intent.getStringExtra("title") ?: ""
        val url = intent.getStringExtra("url") ?: ""

        binding.toFavorites.setOnClickListener {
            Thread {
                db.getDao().insertGif(GifEntity(null, titleText, url))
            }.start()
        }

        binding.title.text = titleText
        Glide.with(this).load(url).into(binding.image)
    }
}