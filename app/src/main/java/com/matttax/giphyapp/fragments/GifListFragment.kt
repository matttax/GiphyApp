package com.matttax.giphyapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.matttax.giphyapp.*
import com.matttax.giphyapp.network.*
import com.matttax.giphyapp.datasource.MainDB
import com.matttax.giphyapp.R
import com.matttax.giphyapp.databinding.FragmentGifListBinding
import com.matttax.giphyapp.datasource.GifDao
import com.matttax.giphyapp.model.GifModel
import com.matttax.giphyapp.model.GifModel.Companion.toGifModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class GifListFragment: Fragment(R.layout.fragment_gif_list) {

    @Inject
    lateinit var gifAPI: GifAPI

    lateinit var binding: FragmentGifListBinding
    lateinit var gifDao: GifDao

    lateinit var searchConfig: SearchConfig
    private val gifs = mutableListOf<GifModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gifDao = MainDB.getDB(requireContext()).getGifDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        navigator().listenResult(viewLifecycleOwner) {
            gifs.removeAt(it)
            binding.gifList.adapter?.notifyItemRemoved(it)
        }
        binding = FragmentGifListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onStart()
        initRecyclerView(gifs)
        searchConfig = SearchConfig(0, SearchType.TRENDING, null)

        binding.search.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?): Boolean {
                    gifs.clear()
                    searchConfig = SearchConfig(0, SearchType.BY_TEXT, text).also {
                        showData(it)
                    }
                    binding.search.clearFocus()
                    return true
                }

                override fun onQueryTextChange(text: String?) = true
            }
        )

        binding.load.setOnClickListener {
            searchConfig = searchConfig.copy(offset = searchConfig.offset + 24).also {
                showData(it)
            }
        }

        binding.favorites.setOnClickListener {
            gifs.clear()
            addSingle(gifDao.getAll()) {
                it.map { g -> GifModel(g.title, g.url, isInFavourites = true) }
            }
            searchConfig = SearchConfig.getDefault()
        }

        binding.trending.setOnClickListener {
            gifs.clear()
            searchConfig = SearchConfig.getDefault().also {
                showData(it)
            }
        }

        showData(searchConfig)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun showData(searchConfig: SearchConfig) {
        val single = if (searchConfig.searchType == SearchType.BY_TEXT) {
            gifAPI.getBySearch(searchConfig.offset, searchConfig.searchText ?: "cats")
        } else {
            gifAPI.getTrending(searchConfig.offset)
        }
        addSingle(single) {
            it.images.map { item -> item.toGifModel() }
        }
    }

    private fun <T> addSingle(single: Single<T>, transform: (T) -> Collection<GifModel>) {
        compositeDisposable.add(single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    gifs.addAll(transform(it))
                    binding.gifList.adapter?.notifyItemRangeChanged(0, 25)
                },
                {
                    Log.e("Unable to load", it.message.toString())
                }
            )
        )
    }

    private fun initRecyclerView(gifs: MutableList<GifModel>) {
        val gifAdapter = GifsAdapter(requireContext(), gifs) {
            navigator().showGif(
                it,
                gifs[it].url,
                gifs[it].title,
                gifs[it].isInFavourites
            )
        }
        binding.gifList.apply {
            adapter = gifAdapter
            layoutManager = FlexboxLayoutManager(requireContext())
        }
    }
}
