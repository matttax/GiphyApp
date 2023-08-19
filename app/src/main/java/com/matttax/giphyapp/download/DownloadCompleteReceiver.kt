package com.matttax.giphyapp.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DOWNLOAD_ACTION) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != -1L) {
                Log.i(TAG, "Download $id completed")
            }
        }
    }

    companion object {
        const val DOWNLOAD_ACTION = "android.intent.action.DOWNLOAD_COMPLETE"
        const val TAG = "Gif downloaded"
    }
}