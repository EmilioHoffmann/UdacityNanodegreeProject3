package com.hoffmann.emilio.project3.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoffmann.emilio.project3.R
import com.hoffmann.emilio.project3.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setListeners()

        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.downloadButton.setOnClickListener {
            handleDownloadClick()
        }
    }

    private fun handleDownloadClick() {
        when (binding.mainRadioGroup.checkedRadioButtonId) {
            R.id.glideRadioButton -> {}
            R.id.loadAppRadioButton -> {}
            R.id.retrofitRadioButton -> {}
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
