package com.gikoko.recap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<View>(R.id.splashLogo)
        val title = findViewById<View>(R.id.splashTitle)
        val subtitle = findViewById<View>(R.id.splashSubtitle)
        val tagline = findViewById<View>(R.id.splashTagline)

        // ---- Cyber 3D entrance animation ----
        logo.alpha = 0f
        logo.scaleX = 0.3f
        logo.scaleY = 0.3f
        title.alpha = 0f
        title.translationY = 40f
        subtitle.alpha = 0f
        subtitle.translationY = 40f
        tagline.alpha = 0f

        logo.animate().alpha(1f).scaleX(1f).scaleY(1f)
            .setDuration(900L)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()

        title.animate().alpha(1f).translationY(0f)
            .setStartDelay(500L).setDuration(700L).start()
        subtitle.animate().alpha(1f).translationY(0f)
            .setStartDelay(800L).setDuration(700L).start()
        tagline.animate().alpha(0.9f)
            .setStartDelay(1100L).setDuration(700L).start()

        // ---- WebView ဆီ ကူးပြောင်း ----
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2600L)
    }
}
