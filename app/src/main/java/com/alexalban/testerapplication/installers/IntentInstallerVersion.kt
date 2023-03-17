package com.alexalban.testerapplication.installers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import com.alexalban.testerapplication.BuildConfig
import java.io.File

class IntentInstallerVersion(private val context: Context) {

    companion object {
        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }

    fun intentInstallation(uri: String){
        println("It started!!!")
        val file = File(uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            val contentUri = FileProvider.getUriForFile(context
//                ,BuildConfig.APPLICATION_ID + ".provider"
//                ,file)
            val install = Intent(Intent.ACTION_VIEW)
            install.setDataAndType(uriFromFile(context, file), MIME_TYPE)
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
//            install.data = contentUri
            try {
                context.startActivity(install)
            } catch (e: ActivityNotFoundException){
                e.printStackTrace();
                Log.e("info", "Error in opening the file!");
            }

        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.setDataAndType(Uri.parse(uri), MIME_TYPE)
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(install)
        }
    }

    fun uriFromFile(context: Context?, file: File?): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", file!!
            )
        } else {
            Uri.fromFile(file)
        }
    }
}