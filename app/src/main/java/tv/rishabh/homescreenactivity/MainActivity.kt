package tv.rishabh.homescreenactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private  lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        val toolBar: Toolbar? = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        wireListeners()
        initializeAds()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    fun initializeAds(){
        MobileAds.initialize(this) {
            Log.i("cat","initializing ad");
            mAdView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()


            mAdView.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    Log.d("cat","on ad loaded")
                }

                override fun onAdFailedToLoad(adError : LoadAdError) {
                    // Code to be executed when an ad request fails.
                    Log.i("cat",adError.domain + adError.responseInfo+"cause is"+adError.cause)
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            }
            mAdView.loadAd(adRequest)
        }
    }

    fun wireListeners(){
        val cameraButton:FloatingActionButton = findViewById(R.id.camera_button)
        cameraButton.setOnClickListener {
            startActivity(Intent(this,CameraActivity::class.java))
        }

    }
}
