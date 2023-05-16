package com.mohgorrah.mah.User

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_photos_posts_user.*
import kotlinx.android.synthetic.main.main_categories_template.view.*

class PhotosPosts : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_posts_user)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        getImages()

    }
    class img(var image: String = "")

    private fun getImages() {

        Firebase.firestore.collection("Settings").document("PhotosPosts").get()
            .addOnSuccessListener {
                val a = it.get("Images") as ArrayList<String>
                val arr = ArrayList<img>()

                a.forEach {
                    arr.add(img(it))
                }
                val adapter = ImagesRecycleView(this, arr)

                PhotosPostsUserRecycle.adapter = adapter

                PhotosPostsUserRecycle.layoutManager = LinearLayoutManager(this)
            }
    }

    class ImagesRecycleView(private val cont: Context, private val List: ArrayList<img>) :
        RecyclerView.Adapter<ViewH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
            return ViewH(
                LayoutInflater.from(cont).inflate(R.layout.photos_posts_template, parent, false)
            )

        }

        override fun onBindViewHolder(holder: ViewH, posi: Int) {
            if (List[posi].image.isNotEmpty())
                holder.itemView.cate_img.load(List[posi].image){
                    placeholder(R.drawable.image)
                }

        }

        override fun getItemCount(): Int {
            return List.size
        }

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)

}