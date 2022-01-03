package com.hoffmann.emilio.project3.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoffmann.emilio.project3.databinding.ActivityDetailsBinding

private lateinit var binding: ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setListeners()

        setContentView(binding.root)
    }

    private fun setListeners() {
    }
}
