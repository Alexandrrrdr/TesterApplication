package com.alexalban.testerapplication

import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.alexalban.testerapplication.databinding.ActivityMainBinding
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter
    private val intentInstallerVersion =  IntentInstallerVersion(app = this)
    private val packageInstallerVersion =  PackageInstallerVersion()

    private val downloader = Downloader(this)

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter{
        return MainPresenter(
            intentInstallerVersion = intentInstallerVersion,
            packageInstallerVersion = packageInstallerVersion,
            downloader = downloader,
            app = this
        )
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInstallIntent.setOnClickListener {
            val loadId = mainPresenter.getAppAndInstallViaIntent()
            val intent = Intent("intentExtra")
            intent.putExtra("extra", loadId)
            sendBroadcast(intent)
            val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            registerReceiver(DownloadCompletedReceiver(), intentFilter)

        }

        binding.btnInstallPackInstaller.setOnClickListener {
            val loadId = mainPresenter.getAppAndInstallViaPackInstaller()
            val intent = Intent("intentExtra")
            intent.putExtra("extra", loadId)
            sendBroadcast(intent)
            val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            registerReceiver(DownloadCompletedReceiver(), intentFilter)

        }


    }

    override fun enableButtons() {
        binding.btnInstallIntent.isEnabled = true
        binding.btnInstallPackInstaller.isEnabled = true
    }

    override fun disableButtons() {
        binding.btnInstallIntent.isEnabled = false
        binding.btnInstallPackInstaller.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(DownloadCompletedReceiver())
        _binding = null
    }
}