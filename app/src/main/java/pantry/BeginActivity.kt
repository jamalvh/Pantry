package com.pantry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class BeginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_begin)

        val beginImage: ImageView = findViewById<View>(R.id.iv_begin) as ImageView

        beginImage.setOnClickListener {
            startActivity(
                Intent(
                    this@BeginActivity,
                    HomeActivity::class.java
                )
            )
        }
    }
}