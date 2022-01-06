package com.hoffmann.emilio.project3.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.hoffmann.emilio.project3.R
import com.hoffmann.emilio.project3.details.DetailsActivity
import com.hoffmann.emilio.project3.utils.Constants.CHANNEL_ID
import java.util.Random

fun NotificationManager.sendNotification(context: Context, downloadItem: DownloadItem) {
    val notificationId = Random().nextInt()
    val detailsIntent = Intent(context, DetailsActivity::class.java)
    detailsIntent.putExtra(Constants.DETAILS_BUNDLE_FILE_NAME, "${downloadItem.name}.zip")
    detailsIntent.putExtra(Constants.DETAILS_BUNDLE_DOWNLOAD_STATUS, downloadItem.downloadSucceed)

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        notificationId,
        detailsIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        context,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_baseline_cloud_download)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description, downloadItem.name))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_baseline_cloud_download,
            context.getString(R.string.notification_button),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(notificationId, builder.build())
}
