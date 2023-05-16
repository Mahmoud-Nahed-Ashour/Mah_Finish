package com.mohgorrah.mah.User

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.api.load
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.*
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        getData()

    }

    override fun onResume() {
        super.onResume()
        getData()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )

        newsNewTv.isSelected = true
        newsNewTv.marqueeRepeatLimit = -1

        Linear1.setOnClickListener {
            val intent = Intent(applicationContext, TotalHoriaActivity::class.java)
            startActivity(intent)

        }
        Linear3.setOnClickListener {
            val intent = Intent(applicationContext, BooksPosts::class.java)
            startActivity(intent)
        }

        imageViews.setOnClickListener {
            startActivity(Intent(this, PhotosPosts::class.java))
        }
        video.setOnClickListener {
            startActivity(Intent(this, VideosPosts::class.java))
        }
        Linear2.setOnClickListener {
            startActivity(Intent(this, AsserCard::class.java))
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.menu_item1 -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun getData() {

        val firestore = FirebaseFirestore.getInstance()
        val imgList = ArrayList<SlideModel>()

        firestore.collection("Settings").document("SliderImages").get().addOnSuccessListener {

            val images: ArrayList<String>? = it.get("Images") as? ArrayList<String>

            images!!.forEach { imgUrl ->
                imgList.add(SlideModel(imgUrl))
            }

            if (imgList.isNotEmpty())
                sliderr.setImageList(imgList)
        }

        Firebase.firestore.collection("Settings").document("News").get()
            .addOnSuccessListener {
                newsNewTv.text = it.get("News").toString()
            }

        Firebase.firestore.collection("Settings").document("PhotosPosts").get()
            .addOnSuccessListener {
                val a = it.get("Images") as ArrayList<String>
                imageViews.load(a[a.size - 1])
            }

        Firebase.firestore.collection("Settings").document("News").get()
            .addOnSuccessListener {
                newsNewTv.text = it.get("News").toString()
            }
    }
}