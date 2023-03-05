package com.matttax.giphyvk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class GifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)

        val image = findViewById<ImageView>(R.id.image)
        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).into(image)
    }
}