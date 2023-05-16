package com.mohgorrah.mah.User

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_videos_posts.*
import kotlinx.android.synthetic.main.video_posts_template.view.*

class VideosPosts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videos_posts)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        getVideos()

    }


    class video(var video: String = "")

    private fun getVideos() {

        Firebase.firestore.collection("Settings").document("VideosPosts").get()
            .addOnSuccessListener {
                val a = it.get("Videos") as ArrayList<String>

                val arr = ArrayList<video>()
                a.forEach {
                    arr.add(video(it))
                }

                val adapter = VideosRecycleView(this, arr)

                VideosPostsUserRecycle.adapter = adapter

                VideosPostsUserRecycle.layoutManager = LinearLayoutManager(this)
            }
    }

    class VideosRecycleView(private val cont: Context, private val List: ArrayList<video>) :
        RecyclerView.Adapter<ViewH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
            return ViewH(
                LayoutInflater.from(cont).inflate(R.layout.video_posts_template, parent, false)
            )

        }

        override fun onBindViewHolder(holder: ViewH, posi: Int) {
            if (List[posi].video.isNotEmpty()) {
                holder.itemView.video.load(R.drawable.image)

//                val retriever = MediaMetadataRetriever()
//                retriever.setDataSource(List[posi].video, HashMap<String, String>())
//                val coverImage = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
//                holder.itemView.video.setImageBitmap(coverImage)

//                val bitmap = ThumbnailUtils.createVideoThumbnail(List[posi].video, MediaStore.Images.Thumbnails.MINI_KIND)
//                holder.itemView.video.setImageBitmap(bitmap)

            }


            holder.itemView.video.setOnClickListener {
                val i = Intent(cont, PreviewVideo::class.java)
                i.putExtra("video", List[posi].video)
                cont.startActivity(i)
            }


        }

        override fun getItemCount(): Int {
            return List.size
        }

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)

}