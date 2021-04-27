package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var radioButtonSelected = false
    private lateinit var downloadItem: String

    private var URL =
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        val channelName = getString(R.string.notification_channel_name)
        createChannel(CHANNEL_ID, channelName)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton.setOnClickListener {
            if (!radioButtonSelected) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_file),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                download()
            }
        }

        setContentView(binding.root)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)?.toInt() ?: -1


            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            val notificationText = getString(R.string.download_text, downloadItem)


            notificationManager.sendNotification(notificationText, applicationContext, id, downloadItem)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    fun onRadioButtonClicked(view: View) {

        radioButtonSelected = true

        val checked = (view as RadioButton).isChecked

        // Check which radio button was clicked
        when (view.id) {
            R.id.radioButton_glide -> {
                if (checked) {
                    URL = "https://github.com/bumptech/glide"
                    downloadItem = getString(R.string.glide)
                }
            }
            R.id.radioButton_loadapp -> {
                if (checked) {
                    URL =
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                    downloadItem = getString(R.string.project_3)
                }
            }
            R.id.radioButton_retrofit -> {
                if (checked) {
                    URL = "https://github.com/square/retrofit"
                    downloadItem = getString(R.string.retrofit)
                }
            }
            else -> throw IllegalArgumentException()
        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        //setup channel settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.WHITE
                enableVibration(true)
                description = "Download finished"
            }
            //create channel
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "channelId"
        const val STATUS_ID = "statusId"
        const val DOWNLOAD_NAME = "downloadName"
    }

}
