package com.pantry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var backdropTakePhoto: ImageView
    private lateinit var backdropLearnMore: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        backdropTakePhoto = findViewById(R.id.iv_takephoto)
        backdropLearnMore = findViewById(R.id.iv_learnmore)

        backdropTakePhoto.setOnClickListener(this)
        backdropLearnMore.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_takephoto -> {

                startActivity(Intent(this@HomeActivity, InfoActivity::class.java))

            }
            R.id.iv_learnmore -> {

                startActivity(Intent(this@HomeActivity, AboutActivity::class.java))

            }
        }
    }

}