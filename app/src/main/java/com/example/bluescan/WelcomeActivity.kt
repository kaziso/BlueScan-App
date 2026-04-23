package com.example.bluescan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val tvWelcome = findViewById<View>(R.id.tvWelcome)

        // Animation: Fade In + Scale Up -> Hold -> Slide Up
        val fadeIn = ObjectAnimator.ofFloat(tvWelcome, "alpha", 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(tvWelcome, "scaleX", 0.8f, 1f)
        val scaleY = ObjectAnimator.ofFloat(tvWelcome, "scaleY", 0.8f, 1f)
        
        val enterSet = AnimatorSet()
        enterSet.playTogether(fadeIn, scaleX, scaleY)
        enterSet.duration = 1000
        enterSet.interpolator = AccelerateDecelerateInterpolator()

        val slideUp = ObjectAnimator.ofFloat(tvWelcome, "translationY", 0f, -200f)
        val fadeOut = ObjectAnimator.ofFloat(tvWelcome, "alpha", 1f, 0f)
        
        val exitSet = AnimatorSet()
        exitSet.playTogether(slideUp, fadeOut)
        exitSet.duration = 800
        exitSet.startDelay = 1500 // Hold for 1.5s

        val fullAnim = AnimatorSet()
        fullAnim.playSequentially(enterSet, exitSet)
        
        fullAnim.start()
        
        // Navigate after total duration (1000 + 1500 + 800 = 3300ms)
        tvWelcome.postDelayed({
            startActivity(Intent(this, FolderSelectionActivity::class.java))
            finish() // Remove from back stack
            // Disable transition animation to make it smooth? Or use default?
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 3300)
    }
}
