package com.matttax.giphyvk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.google.android.flexbox.FlexboxLayoutManager
import com.matttax.giphyvk.databinding.ActivityMainBinding
import com.matttax.giphyvk.retrofit.*
import com.matttax.giphyvk.room.MainDB
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.giphy.com/v1/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = MainDB.getDB(this)
        val gifs = mutableListOf<GeneralData>()
        val searchConfig = SearchConfig(0, SearchType.TRENDING, null)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val gifAPI = retrofit.create(GifAPI::class.java)


        val gifAdapter = GifsAdapter(this, gifs)
        gifAdapter.setOnItemClickListener(object : GifsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, GifActivity::class.java)
                intent.putExtra("url", gifs[position].imageData.gifImage.url)
                intent.putExtra("title", gifs[position].title)
                startActivity(intent)
            }
        })
        binding.gifList.apply {
            adapter = gifAdapter
            layoutManager = FlexboxLayoutManager(this@MainActivity)
        }

        val loader = object: Callback<GifListData> {
            override fun onResponse(call: Call<GifListData>, response: Response<GifListData>) {
                val body = response.body()
                if (body != null) {
                    gifs.addAll(body.images)
                    binding.gifList.adapter?.notifyItemRangeChanged(0, 25)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<GifListData>, t: Throwable) {
                Log.i("load", t.message.toString())
            }
        }


        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                gifs.clear()
                searchConfig.offset = 0
                searchConfig.searchType = SearchType.BY_TEXT
                searchConfig.searchText = text
                gifAPI.getBySearch(searchConfig.offset, searchConfig.searchText ?: "cats").enqueue(loader)
                binding.search.clearFocus()
                return true
            }

            override fun onQueryTextChange(text: String?) = true

        })

        val load = findViewById<Button>(R.id.load)
        load.setOnClickListener {
            searchConfig.offset += 24
            if (searchConfig.searchType == SearchType.TRENDING) {
                gifAPI.getTrending(searchConfig.offset).enqueue(loader)
            } else {
                gifAPI.getBySearch(searchConfig.offset, searchConfig.searchText ?: "cats").enqueue(loader)
            }
        }

        binding.favorites.setOnClickListener {
            gifs.clear()
            db.getDao().getAll().asLiveData().observe(this) {
                val favoritesList = it.map { g -> GeneralData(ImageData(GifImage(g.url)), g.title) }
                gifs.addAll(favoritesList)
                binding.gifList.adapter?.notifyItemRangeChanged(0, favoritesList.size)
            }
            searchConfig.toDefault()
        }

        binding.trending.setOnClickListener {
            gifs.clear()
            gifAPI.getTrending(0).enqueue(loader)
        }

        gifAPI.getTrending(0).enqueue(loader)
    }
}

