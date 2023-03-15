package com.alexalban.testerapplication

import android.content.Context
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
    private val app: Context
) : MvpPresenter<MainView>() {


    fun getAppAndInstallViaIntent(): Long {
        return downloader.downloadFile(MAIN_URL)
    }

    fun getAppAndInstallViaPackInstaller(): Long {
        return downloader.downloadFile(MAIN_URL)
    }
}