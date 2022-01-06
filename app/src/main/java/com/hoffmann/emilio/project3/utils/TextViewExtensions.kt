package com.hoffmann.emilio.project3.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hoffmann.emilio.project3.R

@BindingAdapter("app:bindDownloadStatus")
fun TextView.bindDownloadStatus(downloadSucceeded: Boolean) {
    val statusString = if (downloadSucceeded) {
        R.string.status_success
    } else {
        R.string.status_failed
    }
    text = context.getString(statusString)

    val color =
        if (downloadSucceeded) context.getColor(R.color.valueTextColor) else context.getColor(
            android.R.color.holo_red_dark
        )
    setTextColor(color)
}
