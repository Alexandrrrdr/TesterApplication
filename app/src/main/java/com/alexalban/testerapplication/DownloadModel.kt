package com.alexalban.testerapplication

import android.net.Uri

data class DownloadModel(
    val downloadUri: Uri,
    val isSuccess: Boolean
    //data from backend
//    val spectacleId: Long
)