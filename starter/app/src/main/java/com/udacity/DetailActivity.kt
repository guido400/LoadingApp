package com.udacity

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.MainActivity.Companion.DOWNLOAD_NAME
import com.udacity.MainActivity.Companion.STATUS_ID
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDetailBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        val statusId = intent.getIntExtra(STATUS_ID, -1)
        val downloadName = intent.getStringExtra(DOWNLOAD_NAME)

        val status = when (statusId) {
            DownloadManager.STATUS_SUCCESSFUL -> getString(R.string.success)
            DownloadManager.STATUS_RUNNING -> getString (R.string.running)
            else -> getString (R.string.failed)
        }

        binding.contentDetail.textStatusValue.text = status

        binding.contentDetail.textFilenameValue.text = downloadName

        val okButton = binding.contentDetail.buttonOk

        okButton.setOnClickListener {
            val mainIntent = Intent (this,MainActivity::class.java)
            startActivity(mainIntent)
        }

        setContentView(binding.root)
    }

}
