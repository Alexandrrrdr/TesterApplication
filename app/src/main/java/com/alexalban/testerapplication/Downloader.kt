package com.alexalban.testerapplication

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File

class Downloader(private val context: Context) {

    companion object {
        private const val FILE_NAME = "app_tester.apk"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }
    private val downloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @SuppressLint("Range")
    fun downloadFile(linkAddress: String): Long {
        val uriAddress = Uri.parse(linkAddress)
        val destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/" + FILE_NAME
        val file = File(destination)
        return if (file.exists()) {
            Toast.makeText(context, "File exists, download autocanceled", Toast.LENGTH_SHORT).show()
            0L
        } else {
            Toast.makeText(context, "File doesn't exists, download started", Toast.LENGTH_SHORT).show()
            downloadFile(uriAddress)
        }
    }

    private fun downloadFile(uri: Uri): Long {
        val request = DownloadManager.Request(uri)
            .setMimeType(MIME_TYPE)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(FILE_NAME)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FILE_NAME)
        return downloadManager.enqueue(request)
    }
}