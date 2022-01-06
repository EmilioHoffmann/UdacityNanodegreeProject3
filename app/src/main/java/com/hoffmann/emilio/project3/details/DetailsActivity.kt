package com.hoffmann.emilio.project3.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoffmann.emilio.project3.databinding.ActivityDetailsBinding
import com.hoffmann.emilio.project3.utils.Constants.DETAILS_BUNDLE_DOWNLOAD_STATUS
import com.hoffmann.emilio.project3.utils.Constants.DETAILS_BUNDLE_FILE_NAME

private lateinit var binding: ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        binding.itemName = intent.getStringExtra(DETAILS_BUNDLE_FILE_NAME)
        binding.itemDownloadSucceeded =
            intent.getBooleanExtra(DETAILS_BUNDLE_DOWNLOAD_STATUS, false)

        setListeners()

        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.detailsButton.setOnClickListener {
            finish()
        }
    }
}
