package com.alexalban.testerapplication

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.alexalban.testerapplication.installers.IntentInstallerVersion
import com.alexalban.testerapplication.installers.PackageInstallerVersion
import com.alexalban.testerapplication.utils.Constants.FILE_BASE_PATH
import com.alexalban.testerapplication.utils.Constants.FILE_NAME
import com.alexalban.testerapplication.utils.Constants.MIME_TYPE
import java.io.File

class Downloader(private val context: Context, private val intentInstallerVersion: IntentInstallerVersion, private val packageInstallerVersion: PackageInstallerVersion) {

    private val downloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private var downloadId = 0L

    @SuppressLint("Range")
    fun downloadFile(linkAddress: String, installationType: Int) {
        val uriAddress = Uri.parse(linkAddress)
        val destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + FILE_NAME
        val uri = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)

        if (file.exists()){
            startInstall(installationType, destination, uri)
        } else {
            downloadId = downloadFile(uriAddress)
        }
    }

    private fun startInstall(type: Int, destination: String, uri: Uri){
        when(type){
            1 -> {
                installViaIntent(destination = destination, uri = uri)
            }
            2 -> {
                installViaPackage(destination = destination, uri = uri)
            }
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