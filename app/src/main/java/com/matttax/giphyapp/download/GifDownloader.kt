package com.matttax.giphyapp.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GifDownloader @Inject constructor(
    @ApplicationContext context: Context
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadByUrl(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/gif")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED or DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle(FILE_NAME)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, FILE_NAME)
        return downloadManager.enqueue(request)
    }

    companion object {
        const val FILE_NAME = "picture.gif"
    }
}