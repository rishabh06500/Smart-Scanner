package tv.rishabh.homescreenactivity

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*


class CameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    lateinit var cameraDevice: CameraDevice
    lateinit var textureView: TextureView
    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    lateinit var imageDimension: Size
    var captureRequestBuilder : CaptureRequest.Builder?= null

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice.close()
            cameraDevice = camera
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
            Log.d("cat","opening camera")
        } else {
            textureView.surfaceTextureListener = this

        }
    }


    fun startBackgroundThread() {
        backgroundThread = HandlerThread("Camera Background");
        backgroundThread!!.start();
        backgroundHandler = Handler(backgroundThread?.getLooper()!!);
    }

    fun stopBackgroundThread() {
        backgroundThread?.quitSafely();
        try {
            backgroundThread?.join();
            backgroundThread = null;
            backgroundThread = null;
        } catch (e: InterruptedException) {
            e.printStackTrace();
        }
    }


    @SuppressLint("MissingPermission")
    fun openCamera() {
        val manager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = manager.getCameraIdList()[0];
            val characteristics = manager.getCameraCharacteristics(cameraId);
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = map!!.getOutputSizes(SurfaceTexture::class.java)[0]

            manager.openCamera(cameraId, stateCallback, null);
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
    openCamera()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        textureView = findViewById(R.id.texture)
        textureView.surfaceTextureListener = this
    }

    fun createCameraPreview() {
      Log.d("cat","creating camera preview")
        try {
            val texture = textureView.getSurfaceTexture()
            texture?.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            val surface = Surface(texture);
             captureRequestBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder?.apply {  addTarget(surface)}
            cameraDevice.createCaptureSession(
                Arrays.asList(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(p0: CameraCaptureSession) {
                        this@CameraActivity.cameraCaptureSessions = p0;
                        updatePreview()
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                   //print error
                    }

                },
                null
            );
        } catch (e: CameraAccessException) {
            Log.d("cat","trouble accessing camera")
            e.printStackTrace();
        }
    }



    fun updatePreview() {

        captureRequestBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            captureRequestBuilder?.build()?.let {
                cameraCaptureSessions?.setRepeatingRequest(
                    it,
                    null,
                    backgroundHandler
                )
            };
        } catch (e:CameraAccessException) {
            e.printStackTrace();
        }
    }


}



