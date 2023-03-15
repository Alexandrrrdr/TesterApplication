package com.alexalban.testerapplication

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class DownloadCompletedReceiver(): BroadcastReceiver() {

    private lateinit var downloadManager: DownloadManager

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        downloadManager = context.getSystemService(DownloadManager::class.java)
        val broadcastDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        var downloadingId = 0L
        val intentAction = intent.action
        if (intentAction == "intentExtra"){
            downloadingId = intent.getLongExtra("extra", 0L)
        }
        if (broadcastDownloadId == downloadingId){
            if (getDownloadStatus(id = downloadingId, dm = downloadManager) == DownloadManager.STATUS_SUCCESSFUL){
                Toast.makeText(context, "Download complete $downloadingId", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Download incomplete $downloadingId", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDownloadStatus(id: Long, dm: DownloadManager): Int{
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor: Cursor = dm.query(query)

        if (cursor.moveToFirst()){
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status = cursor.getInt(columnIndex)
            return status
        }
        return DownloadManager.ERROR_UNKNOWN
    }
}