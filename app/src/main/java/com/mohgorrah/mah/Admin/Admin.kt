package com.mohgorrah.mah.Admin

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_admin.*

class Admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        imagesSliderbtn.setOnClickListener {
            startActivity(Intent(this, ImagesSlider::class.java))
        }

        postsimagesbtn.setOnClickListener {
            startActivity(Intent(this, PhotosPosts::class.java))
        }
        postsVidoesbtn.setOnClickListener {
            startActivity(Intent(this, VideoPosts::class.java))
        }
        AseerBtn.setOnClickListener {
            startActivity(Intent(this, aseer::class.java))
        }
        BooksBtn.setOnClickListener {
            startActivity(Intent(this, BooksPosts::class.java))
        }
        newsBtn.setOnClickListener {
            startActivity(Intent(this, NewsEdit::class.java))
        }
    }
}