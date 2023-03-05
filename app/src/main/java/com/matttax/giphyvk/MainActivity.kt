package com.matttax.giphyvk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.giphy.com/v1/"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val gifs = mutableListOf<DataObject>()
        var offset = 0
        var searchText = ""
        var trending = true

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.gifList)
        val adapter = GifsAdapter(this, gifs)
        rv.adapter = adapter
        rv.setHasFixedSize(true)
        rv.layoutManager = FlexboxLayoutManager(this)
        adapter.setOnItemClickListener(object : GifsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, GifActivity::class.java)
                intent.putExtra("url", gifs[position].images.ogImage.url)
                startActivity(intent)
            }
        })

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val retroService = retrofit.create(DataService::class.java)
        val loader = object: Callback<DataResult?> {
            override fun onResponse(call: Call<DataResult?>, response: Response<DataResult?>) {
                val body = response.body()
                gifs.clear()
                if (body != null) {
                    gifs.addAll(body.res)
                }
            }
            override fun onFailure(call: Call<DataResult?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Cannot connect", Toast.LENGTH_LONG)
            }
        }

        retroService.getTrendingGifs(0).enqueue(loader)

        val next = findViewById<Button>(R.id.nextButton)
        val back = findViewById<Button>(R.id.previousButton)
        val pageText = findViewById<TextView>(R.id.page)

        back.isInvisible = true

        next.setOnClickListener {
            gifs.clear()
            offset += 25
            pageText.text = "Page " + (offset / 25)
            if (trending) {
                retroService.getTrendingGifs(offset).enqueue(loader)
            } else {
                retroService.searchGifs(searchText, offset)
            }
            if (offset >= 25)
                back.isInvisible = false
        }

        back.setOnClickListener {
            gifs.clear()
            offset -= 25
            pageText.text = "Page " + (offset / 25)
            if (trending) {
                retroService.getTrendingGifs(offset).enqueue(loader)
            } else {
                retroService.searchGifs(searchText, offset)
            }
            if (offset == 0) {
                back.isInvisible = true
            }
        }

        val editText = findViewById<EditText>(R.id.searchBar)
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                gifs.clear()
                searchText = editText.text.toString()
                trending = false
                offset = 0
                retroService.searchGifs(searchText, offset).enqueue(loader)
            }
            true
        }


    }
}

