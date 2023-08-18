package com.matttax.giphyapp

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

interface Navigator {
    fun showGif(position: Int, url: String, title: String, isFavorite: Boolean)
    fun goBack()
    fun listenResult(
        lifecycleOwner: LifecycleOwner,
        resultListener: ResultListener<Int>
    )
    fun publishResult(result: Int)
}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

typealias ResultListener<T> = (T) -> Unit

