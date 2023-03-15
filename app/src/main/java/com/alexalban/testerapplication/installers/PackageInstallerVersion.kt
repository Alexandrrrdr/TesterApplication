package com.alexalban.testerapplication.installers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import com.alexalban.testerapplication.broadcasts.PackageInstallReceiver
import com.alexalban.testerapplication.utils.Constants

class PackageInstallerVersion() {

    private val packageInstalledAction =
        "com.example.testappinstaller.data.repository.SESSION_API_PACKAGE_INSTALLED"

    companion object {
        private const val APK_NAME = "app_tester.apk"
        private const val PACKAGE = "package"
        private const val PACKAGE_NAME = "com.example.testappinstaller"
    }

    fun packageInstallerDownloader(apkUri: Uri, context: Context) {
        val installer = context.packageManager.packageInstaller
        val resolver = context.contentResolver
        resolver.openInputStream(apkUri)?.use { apkStream ->
            var session: PackageInstaller.Session? = null

            val params: PackageInstaller.SessionParams = PackageInstaller
                .SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId: Int = installer.createSession(params)
            session = installer.openSession(sessionId)

            session.openWrite(PACKAGE, 0, -1).use { packageInSession ->
                apkStream.copyTo(packageInSession)
                session.fsync(packageInSession)
            }

            val intent = Intent(context, PackageInstallReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                Constants.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
//            val intent = Intent(app, PackageInstallerVersion::class.java)
//            intent.action = packageInstalledAction
//            val pendingIntent: PendingIntent = PendingIntent.getActivity(app, 0, intent, 0)
//            val intentSender: IntentSender = pendingIntent.intentSender
//            session.commit(intentSender)

            session.commit(pendingIntent.intentSender)
            session.close()
        }
    }

}