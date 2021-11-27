package com.bytedance.camera.demo

import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VideoRecordActivity : AppCompatActivity() {
    private var mTakePhoto: Button? = null
    private var mRecordVideo: Button? = null
    private var mSurfaceView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mIsRecording = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_record)
        mSurfaceView = findViewById(R.id.surface_view)
        mTakePhoto = findViewById(R.id.take_photo)
        mRecordVideo = findViewById(R.id.record_video)
        startCamera()
        mTakePhoto?.setOnClickListener(View.OnClickListener { takePicture() })
        mRecordVideo?.setOnClickListener(View.OnClickListener { recordVideo() })
    }

    private fun startCamera() {
        try {
            mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK)
            setCameraDisplayOrientation()
        } catch (e: Exception) {
            // error
        }
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    /**
                     *   补充完整缺失代码 C1
                     */
                } catch (e: IOException) {
                    // error
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                try {
                    mCamera!!.stopPreview()
                } catch (e: Exception) {
                    // error
                }
                try {
                    mCamera!!.setPreviewDisplay(holder)
                    mCamera!!.startPreview()
                } catch (e: Exception) {
                    //error
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })
    }

    private fun setCameraDisplayOrientation() {
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        val info = CameraInfo()
        Camera.getCameraInfo(CameraInfo.CAMERA_FACING_BACK, info)
        val result = (info.orientation - degrees + 360) % 360
        mCamera!!.setDisplayOrientation(result)
    }

    private fun takePicture() {
        mCamera!!.takePicture(null, null, PictureCallback { bytes, camera ->
            val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: return@PictureCallback
            try {
                /**
                 *   补充完整缺失代码 C2
                 */
            } catch (e: FileNotFoundException) {
                //error
            } catch (e: IOException) {
                //error
            }
            mCamera!!.startPreview()
        })
    }

    private fun getOutputMediaFile(type: Int): File? {
        // Android/data/com.bytedance.camera.demo/files/Pictures
        val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File
        mediaFile = if (type == MEDIA_TYPE_IMAGE) {
            File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        } else if (type == MEDIA_TYPE_VIDEO) {
            File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")
        } else {
            return null
        }
        return mediaFile
    }

    private fun prepareVideoRecorder(): Boolean {
        mMediaRecorder = MediaRecorder()
        mCamera!!.unlock()
        mMediaRecorder!!.setCamera(mCamera)
        mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mMediaRecorder!!.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
        mMediaRecorder!!.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString())
        mMediaRecorder!!.setPreviewDisplay(mSurfaceHolder!!.surface)
        try {
            mMediaRecorder!!.prepare()
        } catch (e: IllegalStateException) {
            releaseMediaRecorder()
            return false
        } catch (e: IOException) {
            releaseMediaRecorder()
            return false
        }
        return true
    }

    private fun recordVideo() {
        if (mIsRecording) {
            mMediaRecorder!!.stop()
            releaseMediaRecorder()
            mCamera!!.lock()
            mIsRecording = false
            mRecordVideo!!.text = "Start Recording"
        } else {
            if (prepareVideoRecorder()) {
                mMediaRecorder!!.start()
                mIsRecording = true
                mRecordVideo!!.text = "Stop Recording"
            } else {
                releaseMediaRecorder()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaRecorder()
        releaseCamera()
    }

    private fun releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            mCamera!!.lock()
        }
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    companion object {
        private const val MEDIA_TYPE_IMAGE = 1
        private const val MEDIA_TYPE_VIDEO = 2
    }
}