package com.alexalban.testerapplication

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.alexalban.testerapplication.installers.IntentInstallerVersion
import com.alexalban.testerapplication.installers.PackageInstallerVersion
import com.alexalban.testerapplication.utils.Constants.MAIN_URL
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class MainPresenter(
    private val intentInstallerVersion: IntentInstallerVersion,
    private val packageInstallerVersion: PackageInstallerVersion,
    private val downloader: Downloader,
    private val context: Context
) : MvpPresenter<MainView>() {

    companion object {
        private const val FILE_NAME = "app_tester.apk"
//        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }

    private val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .toString() + "/" + FILE_NAME
//    private val downloadManager by lazy {
//        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    }

    fun getFile() {
        val loadid = downloader.downloadFile(MAIN_URL)
        val intent = Intent("intentExtra")
        intent.putExtra("extra", loadid)
        context.sendBroadcast(intent)
        viewState.downloaded(path = path, loadingId = loadid)
    }

    fun getAppAndInstallViaPackInstaller(): Long {
        return downloader.downloadFile(MAIN_URL)
    }


//    @SuppressLint("Range")
//    private fun downlaodFile(linkAddress: String): Long {
//        val uriAddress = Uri.parse(linkAddress)
//        destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            .toString() + "/" + FILE_NAME
//        val file = File(destination)
//        return if (file.exists()) {
////            Toast.makeText(context, "File exists, download autocanceled", Toast.LENGTH_SHORT).show()
//            1L
//        } else {
////            Toast.makeText(context, "File doesn't exists, download started", Toast.LENGTH_SHORT).show()
//            getFile(uriAddress)
//        }
//    }
//
//    private fun getFile(uri: Uri): Long {
//        val request = DownloadManager.Request(uri)
//            .setMimeType(MIME_TYPE)
//            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
//            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            .setTitle(FILE_NAME)
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FILE_NAME)
//        return downloadManager.enqueue(request)
//    }


}