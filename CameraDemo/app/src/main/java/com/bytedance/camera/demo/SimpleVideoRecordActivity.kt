package com.bytedance.camera.demo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SimpleVideoRecordActivity : AppCompatActivity() {
    private var mRecordButton: Button? = null
    private var mVideoView: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_video_record)
        mRecordButton = findViewById(R.id.record)
        mVideoView = findViewById(R.id.video_view)
        mRecordButton?.setOnClickListener(View.OnClickListener {
            /**
             *   补充完整缺失代码 B1
             */

                val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)

//            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoURI = data!!.data
            /**
             *   补充完整缺失代码 B2
             */
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