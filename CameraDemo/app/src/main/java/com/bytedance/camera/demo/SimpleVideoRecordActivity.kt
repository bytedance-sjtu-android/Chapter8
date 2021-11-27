package com.bytedance.camera.demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SimpleVideoRecordActivity : AppCompatActivity() {
    private var mRecordButton: Button? = null
    private var mVideoView: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_video_record)
        mRecordButton = findViewById(R.id.record)
        mVideoView = findViewById(R.id.video_view)
        mRecordButton?.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@SimpleVideoRecordActivity,
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@SimpleVideoRecordActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {

                val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoURI = data!!.data
            mVideoView!!.setVideoURI(videoURI)
            mVideoView!!.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
        }
    }

    companion object {
        const val REQUEST_VIDEO_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 2
    }



}