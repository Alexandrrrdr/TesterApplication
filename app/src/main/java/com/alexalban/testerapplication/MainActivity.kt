package com.alexalban.testerapplication

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter
    private val intentInstallerVersion =  IntentInstallerVersion(context = this)
    private val packageInstallerVersion =  PackageInstallerVersion(context = this)
    private val downloader = Downloader(this)
    private var path: String? = null
    private var loadingId: Long = 0L
    @RequiresApi(Build.VERSION_CODES.O)
    private val permissions: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.REQUEST_INSTALL_PACKAGES,
        Manifest.permission.REQUEST_DELETE_PACKAGES )

    @ProvidePresenter
    fun provideMainPresenter(): MainPresenter{
        return MainPresenter(
            downloader = downloader,
            context = this,
            intentInstallerVersion = intentInstallerVersion,
            packageInstallerVersion = packageInstallerVersion
        )
    }

    private val permissionsContract = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permissionStatusMap ->
        if (!permissionStatusMap.containsValue(value = false)){
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private var writePermission = false
    private var readPermission = false
    private var installPermission = false
    private var deletePermission = false


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInstallIntent.setOnClickListener {
            if (checkPermissions()) {
                mainPresenter.getFile()
            } else {
                permissionsContract.launch(permissions)
            }
        }

        binding.btnInstallPackInstaller.setOnClickListener {
            val loadId = mainPresenter.getAppAndInstallViaPackInstaller()
            sendDataToBroadcast(loadId)
        }
    }

    private fun sendDataToBroadcast(id: Long) {
        val intent = Intent("intentExtra")
        intent.putExtra("extra", id)
        sendBroadcast(intent)
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(DownloadCompletedReceiver(), intentFilter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermissions() {
        readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        installPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED
        deletePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_DELETE_PACKAGES) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()
        if (!readPermission) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!writePermission) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!installPermission) {
            permissionRequest.add(Manifest.permission.REQUEST_INSTALL_PACKAGES)
        }
        if (!deletePermission) {
            permissionRequest.add(Manifest.permission.REQUEST_DELETE_PACKAGES)
        }
        permissionsLauncher.launch(permissionRequest.toTypedArray())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun installPermission(){
        when{
            ContextCompat.checkSelfPermission(this, permissions)
        }
    }



    override fun installed() {
        Snackbar.make(binding.root, "Installation is finished successful", Snackbar.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downloaded(path: String, loadingId: Long) {
        this.path = path
        this.loadingId = loadingId
//        Snackbar.make(binding.root, "Download is finished successful", Snackbar.LENGTH_SHORT).show()
        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(DownloadCompletedReceiver())
        unregisterReceiver(IntentInstallReceiver())
        unregisterReceiver(PackageInstallReceiver())
        _binding = null
    }
}