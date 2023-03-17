package com.alexalban.testerapplication

import com.alexalban.testerapplication.utils.Constants.INTENT_INSTALL
import com.alexalban.testerapplication.utils.Constants.MAIN_URL
import com.alexalban.testerapplication.utils.Constants.PACKAGE_INSTALL
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class MainPresenter(
    private val downloader: Downloader
) : MvpPresenter<MainView>() {

    fun getAppAndInstallViaIntent() {
        downloader.downloadFile(MAIN_URL, installationType = INTENT_INSTALL)
    }

    fun getAppAndInstallViaPackInstaller(){
        downloader.downloadFile(MAIN_URL, installationType = PACKAGE_INSTALL)
    }
}