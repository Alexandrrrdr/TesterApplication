package com.alexalban.testerapplication

import android.content.Context
import com.alexalban.testerapplication.Constants.MAIN_URL
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
        viewState.disableButtons()
        val downloadId = downloader.downloadFile(MAIN_URL)
        viewState.enableButtons()
        return downloadId
    }

    fun getAppAndInstallViaPackInstaller(): Long {
        viewState.disableButtons()
        val downloadId = downloader.downloadFile(MAIN_URL)
        viewState.enableButtons()
        return downloadId
    }
}