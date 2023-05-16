package com.mohgorrah.mah.Admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mohgorrah.mah.App
import com.mohgorrah.mah.R
import com.mohgorrah.mah.User.PreviewVideo
import kotlinx.android.synthetic.main.activity_video_posts.*
import kotlinx.android.synthetic.main.main_categories_template.view.*
import kotlinx.android.synthetic.main.video_posts_template.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class VideoPosts : AppCompatActivity() {

    var newVideo = ""
    lateinit var
            VideosPosts: ArrayList<String>
    lateinit var progDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_posts)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )

        VideosPosts = ArrayList()

        progDialog = ProgressDialog(this)
        progDialog.setTitle("جاري التحميل")
        progDialog.setMessage("جاري رفع الفيديو...")
        progDialog.setCancelable(false)

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    progDialog.show()
//                    progressDialog.show()
                    // There are no request codes
                    val intent: Intent? = result.data
                    val uri = intent?.data  //The uri with the location of the file
                    val file = getFile(this, uri!!)
                    val new_uri = Uri.fromFile(file)

                    val reference =
                        Firebase.storage.reference.child("Videos/${new_uri.lastPathSegment}")
                    val uploadTask = reference.putFile(new_uri)

                    uploadTask.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            progDialog.dismiss()
                            DoneBtnVid.visibility = View.VISIBLE

                            newVideo = it.toString()
                            newVid.load(newVideo)
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        addNewVideo.setOnClickListener {
//            newVid.visibility = View.VISIBLE
//            DoneBtnVid.visibility = View.VISIBLE

            Firebase.firestore.collection("Settings").document("VideosPosts").get()
                .addOnSuccessListener {

                    VideosPosts = it.get("Videos") as ArrayList<String>

                }

            val intent = Intent()
                .setType("video/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            resultLauncher.launch(Intent.createChooser(intent, "Select Vidoe"))

        }

        DoneBtnVid.setOnClickListener {

//            newVid.visibility = View.INVISIBLE
//            DoneBtnVid.visibility = View.INVISIBLE
//

            VideosPosts.add(newVideo)

            val videos = mapOf(
                "Videos" to
                        VideosPosts
            )

            Firebase.firestore.collection("Settings").document("VideosPosts")
                .set(videos).addOnSuccessListener {
                }

            getVideos()
        }

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

                VideosPostsRecycle.adapter = adapter

                VideosPostsRecycle.layoutManager = GridLayoutManager(this, 2)
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
//                val retriever = MediaMetadataRetriever()
//                retriever.setDataSource(List[posi].video, HashMap<String, String>())
//                val coverImage =
//                    retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
//                holder.itemView.video.setImageBitmap(coverImage)
                holder.itemView.video.load(R.drawable.image)

            }
            holder.itemView.video.setOnClickListener {
                val i = Intent(cont, PreviewVideo::class.java)
                i.putExtra("video", List[posi].video)
                cont.startActivity(i)
            }
            holder.itemView.video.setOnLongClickListener {

                Firebase.firestore.collection("Settings").document("VideosPosts").get()
                    .addOnSuccessListener {
                        val VideosPosts = ArrayList<String>()
                        val a = it.get("Videos") as ArrayList<String>
                        a.forEach { currentVid ->
                            if (currentVid != List[posi].video)

                                VideosPosts.add(currentVid)
                        }
                        val Videos = mapOf(
                            "Videos" to
                                    VideosPosts
                        )
                        Firebase.firestore.collection("Settings").document("VideosPosts")
                            .set(Videos)
                        val activity = cont as? Activity
                        activity?.recreate()
                    }


                return@setOnLongClickListener true
            }

        }

        override fun getItemCount(): Int {
            return List.size
        }

    }

    class ViewH(i: View) : RecyclerView.ViewHolder(i)


    fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename: File =
            File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                createFileFromStream(
                    ins!!,
                    destinationFilename
                )
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    private fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor = context.getContentResolver().query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }


}