package com.hoffmann.emilio.project3.home

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoffmann.emilio.project3.R
import com.hoffmann.emilio.project3.databinding.ActivityMainBinding
import com.hoffmann.emilio.project3.utils.DownloadOption

private lateinit var binding: ActivityMainBinding
private lateinit var viewModel: MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            // TODO GENERATE NOTIFICATION
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {}
                DownloadManager.STATUS_FAILED -> {}
            }
        }
    }

    private fun handleDownloadClick() {
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        when (binding.mainRadioGroup.checkedRadioButtonId) {
            R.id.glideRadioButton -> {
                viewModel.download(manager, DownloadOption.GLIDE)
            }
            R.id.loadAppRadioButton -> {
                viewModel.download(manager, DownloadOption.LOAD_APP)
            }
            R.id.retrofitRadioButton -> {
                viewModel.download(manager, DownloadOption.RETROFIT)
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
}
