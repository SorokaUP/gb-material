package ru.sorokin.gb_material.view.main

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.view.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val image = findViewById<ImageView>(R.id.splash_image_view)
        image
            .animate()
            .rotationY(360F)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(2000)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
            .start()
    }
}