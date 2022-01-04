package com.hoffmann.emilio.project3.home

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoffmann.emilio.project3.utils.DownloadOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _downloadProgress = MutableLiveData<Int>()
    val downloadProgress: LiveData<Int>
        get() = _downloadProgress

    private val _downloadStatus = MutableLiveData<Int>()
    val downloadStatus: LiveData<Int>
        get() = _downloadStatus

    @SuppressLint("Range")
    fun download(downloadManager: DownloadManager, downloadOption: DownloadOption) {
        val request = DownloadManager.Request(Uri.parse(downloadOption.url))

        request.setTitle("${downloadOption.name}.zip")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${downloadOption.name}.zip"
        )

        val downloadId = downloadManager.enqueue(request)
        request.setNotificationVisibility(View.VISIBLE)
        var bytesTotal: Long = -1L

        viewModelScope.launch(Dispatchers.IO) {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query()
                query.setFilterById(downloadId)
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()

                val bytesDownloaded: Long = cursor.getLong(
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )

                if (bytesTotal == -1L) bytesTotal =
                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_FAILED || status == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }

                checkDownloadStatus(cursor)

                if (bytesTotal >= 0L) {
                    ((bytesDownloaded * 100L) / bytesTotal).toInt().let { progress ->
                        if (progress > -1L) _downloadProgress.postValue(progress)
                    }
                }
                cursor.close()
            }
        }
    }

    private fun checkDownloadStatus(cursor: Cursor) {
        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        when (cursor.getInt(columnIndex)) {
            DownloadManager.STATUS_FAILED -> {
                _downloadStatus.postValue(DownloadManager.STATUS_FAILED)
                _downloadProgress.postValue(0)
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                _downloadProgress.postValue(100)
                _downloadStatus.postValue(DownloadManager.STATUS_SUCCESSFUL)
            }
        }
    }
}
