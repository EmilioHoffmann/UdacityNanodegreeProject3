package com.hoffmann.emilio.project3.home

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hoffmann.emilio.project3.R
import com.hoffmann.emilio.project3.databinding.ActivityMainBinding
import com.hoffmann.emilio.project3.utils.Constants.CHANNEL_ID
import com.hoffmann.emilio.project3.utils.DownloadItem
import com.hoffmann.emilio.project3.utils.sendNotification

private lateinit var binding: ActivityMainBinding
private lateinit var viewModel: MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createChannel()
        viewModel = MainViewModel()
        setListeners()
        setObservers()
        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.downloadButton.setOnClickListener {
            handleDownloadClick()
        }
    }

    private fun setObservers() {
        viewModel.downloadProgress.observe(this) {
            println("Download Progress = $it")
        }

        viewModel.downloadStatus.observe(this) { status ->
            println("Download Status = $status")
            sendNotification(status)
        }
    }

    private fun handleDownloadClick() {
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        when (binding.mainRadioGroup.checkedRadioButtonId) {
            R.id.glideRadioButton -> {
                viewModel.download(manager, DownloadItem.GLIDE)
            }
            R.id.loadAppRadioButton -> {
                viewModel.download(manager, DownloadItem.LOAD_APP)
            }
            R.id.retrofitRadioButton -> {
                viewModel.download(manager, DownloadItem.RETROFIT)
            }
            else -> {
                Toast.makeText(
                    this,
                    getString(R.string.no_option_selected_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = getColor(R.color.colorPrimary)
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun sendNotification(downloadItem: DownloadItem) {
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(this, downloadItem)
    }
}
