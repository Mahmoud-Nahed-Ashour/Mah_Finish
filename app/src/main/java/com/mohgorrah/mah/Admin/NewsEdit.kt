package com.mohgorrah.mah.Admin

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_news_edit.*

class NewsEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_edit)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        Firebase.firestore.collection("Settings").document("News").get()
            .addOnSuccessListener {
                editTxtNews.setText(it.get("News").toString())
            }

        editNewsBtn.setOnClickListener {
            Firebase.firestore.collection("Settings").document("News").set(mapOf("News" to editTxtNews.text.toString()))
                .addOnSuccessListener {
                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                }
        }

    }
}