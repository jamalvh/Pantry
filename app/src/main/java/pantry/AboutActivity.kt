package com.pantry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backImage: ImageView = findViewById<View>(R.id.iv_about_back) as ImageView

        backImage.setOnClickListener {
            startActivity(
                Intent(
                    this@AboutActivity,
                    HomeActivity::class.java
                )
            )
        }
    }
}