package com.alexalban.testerapplication.installers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.alexalban.testerapplication.BuildConfig

class IntentInstallerVersion(private val context: Context) {

    companion object {
        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }

    fun intentInstallation(uri: Uri){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val contentUri = FileProvider.getUriForFile(context
                ,BuildConfig.APPLICATION_ID + ".provider"
                ,uri.toFile())
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            context.startActivity(install)

        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.setDataAndType(uri, MIME_TYPE)
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(install)
        }
    }
}