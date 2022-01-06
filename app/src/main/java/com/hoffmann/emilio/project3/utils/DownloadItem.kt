package com.hoffmann.emilio.project3.utils

enum class DownloadItem(val url: String, var downloadSucceed: Boolean? = null) {
    GLIDE("https://github.com/bumptech/glide/archive/refs/heads/master.zip"),
    LOAD_APP("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"),
    RETROFIT("https://github.com/square/retrofit/archive/refs/heads/master.zip")
}
