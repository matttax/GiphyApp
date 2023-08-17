package com.matttax.giphyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import com.matttax.giphyapp.databinding.ActivityMainBinding
import com.matttax.giphyapp.fragments.GifListFragment
import com.matttax.giphyapp.fragments.WatchGifFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, GifListFragment())
            .commit()
    }

    override fun showGif(position: Int, url: String, title: String, isFavorite: Boolean) {
        val fragment = WatchGifFragment.newInstance(position, url, title, isFavorite)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.watchGifContainer, fragment, GIF_FRAGMENT_KEY)
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun publishResult(result: Int) {
        supportFragmentManager
            .setFragmentResult(Int::class.simpleName.toString(), bundleOf(KEY_GIF_ADDED_RESULT to result))
    }

    override fun listenResult(
        lifecycleOwner: LifecycleOwner,
        resultListener: ResultListener<Int>
    ) {
        supportFragmentManager.setFragmentResultListener(Int::class.simpleName.toString(), lifecycleOwner) { _, bundle ->
            resultListener(bundle.getInt(KEY_GIF_ADDED_RESULT))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(GIF_FRAGMENT_KEY)
        if (fragment == null) {
            super.onBackPressed()
        } else {
            supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    companion object {
        @JvmStatic private val GIF_FRAGMENT_KEY = "gif"
        @JvmStatic private val KEY_GIF_ADDED_RESULT = "gif_added_result"
    }

}
