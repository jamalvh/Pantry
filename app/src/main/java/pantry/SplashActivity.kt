package com.pantry

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var ivSplash: ImageView = findViewById(R.id.iv_label)

        ivSplash.alpha = 0f
        ivSplash.animate().setDuration(2500).alpha(1f).withEndAction {
            val i = Intent(this, BeginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}