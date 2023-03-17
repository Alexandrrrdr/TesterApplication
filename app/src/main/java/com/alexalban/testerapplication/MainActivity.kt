package com.alexalban.testerapplication

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.alexalban.testerapplication.broadcasts.DownloadCompletedReceiver
import com.alexalban.testerapplication.broadcasts.IntentInstallReceiver
import com.alexalban.testerapplication.broadcasts.PackageInstallReceiver
import com.alexalban.testerapplication.databinding.ActivityMainBinding
import com.alexalban.testerapplication.installers.IntentInstallerVersion
import com.alexalban.testerapplication.installers.PackageInstallerVersion
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter


class MainActivity : MvpAppCompatActivity(), MainView {

    val temp = "https://androidwave.com/download-and-install-apk-programmatically/"

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter
    private val intentInstallerVersion =  IntentInstallerVersion(context = this)
    private val packageInstallerVersion =  PackageInstallerVersion(context = this)
    private val downloader = Downloader(this, intentInstallerVersion, packageInstallerVersion)

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter{
        return MainPresenter(
            downloader = downloader
        )
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInstallIntent.setOnClickListener {
            mainPresenter.getAppAndInstallViaIntent()
        }

        binding.btnInstallPackInstaller.setOnClickListener {
            mainPresenter.getAppAndInstallViaPackInstaller()
        }
    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(applicationContext)
        builder.setTitle("Permission required")
        builder.setMessage("Some permissions are needed to be allowed to use this app without any problems.")
        builder.setPositiveButton("Grant") { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.fromParts("package", applicationContext.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun installed() {
        Snackbar.make(binding.root, "Installation is finished successful", Snackbar.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downloaded() {
        Snackbar.make(binding.root, "Download is finished successful", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(DownloadCompletedReceiver())
        unregisterReceiver(IntentInstallReceiver())
        unregisterReceiver(PackageInstallReceiver())
        _binding = null
    }
}