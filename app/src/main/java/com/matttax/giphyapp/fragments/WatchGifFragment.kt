package com.matttax.giphyapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.matttax.giphyapp.R
import com.matttax.giphyapp.databinding.FragmentWatchGifBinding
import com.matttax.giphyapp.datasource.GifDao
import com.matttax.giphyapp.navigator
import com.matttax.giphyapp.datasource.GifEntity
import com.matttax.giphyapp.datasource.MainDB

class WatchGifFragment: Fragment(R.layout.fragment_watch_gif) {

    lateinit var binding: FragmentWatchGifBinding
    lateinit var gifDao: GifDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gifDao = MainDB.getDB(requireContext()).getGifDao()
        binding = FragmentWatchGifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onStart()
        val titleText = arguments?.getString(ARGUMENTS_KEY_TITLE) ?: ""
        val url = arguments?.getString(ARGUMENTS_KEY_URL) ?: ""
        val isFavorite = arguments?.getBoolean(ARGUMENTS_IS_FAVORITE) ?: false

        if (isFavorite) {
            binding.toFavorites.text = "Remove from favorites"
            binding.toFavorites.setOnClickListener {
                navigator().publishResult(arguments?.getInt(ARGUMENTS_POSITION) ?: -1)
                Thread {
                    gifDao.deleteByUrl(url)
                }.start()
                navigator().goBack()
            }
        } else {
            binding.toFavorites.setOnClickListener {
                navigator().publishResult(arguments?.getInt(ARGUMENTS_POSITION) ?: -1)
                Thread {
                    gifDao.insertGif(GifEntity(null, titleText, url))
                }.start()
            }
        }

        binding.title.text = titleText
        Glide.with(this)
            .load(url)
            .into(binding.image)
    }

    companion object {
        @JvmStatic private val ARGUMENTS_POSITION = "position"
        @JvmStatic private val ARGUMENTS_KEY_URL = "url"
        @JvmStatic private val ARGUMENTS_KEY_TITLE = "title"
        @JvmStatic private val ARGUMENTS_IS_FAVORITE = "is_favorite"

        fun newInstance(position: Int, url: String, title: String, isFavorite: Boolean): WatchGifFragment {
            val args = Bundle().apply {
                putInt(ARGUMENTS_POSITION, position)
                putString(ARGUMENTS_KEY_URL, url)
                putString(ARGUMENTS_KEY_TITLE, title)
                putBoolean(ARGUMENTS_IS_FAVORITE, isFavorite)
            }
            val fragment = WatchGifFragment()
            fragment.arguments = args
            return fragment
        }
    }

}