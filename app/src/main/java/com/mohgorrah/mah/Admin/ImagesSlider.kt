package com.mohgorrah.mah.Admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.ColorDrawable
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mohgorrah.mah.App
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_images_slider.*
import kotlinx.android.synthetic.main.main_categories_template.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ImagesSlider : AppCompatActivity() {

    var newImage = ""
    lateinit var sliderImages: ArrayList<String>
    lateinit var firestore: FirebaseFirestore
    lateinit var progDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_slider)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )


        firestore = FirebaseFirestore.getInstance()

        sliderImages = ArrayList()

        progDialog = ProgressDialog(this)
        progDialog.setTitle("جاري التحميل")
        progDialog.setMessage("جاري رفع الصورة...")
        progDialog.setCancelable(false)


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    progDialog.show()
                    // There are no request codes
                    val intent: Intent? = result.data
                    val uri = intent?.data  //The uri with the location of the file
                    val file = getFile(this, uri!!)
                    val new_uri = Uri.fromFile(file)

                    val reference =
                        Firebase.storage.reference.child("Images/${new_uri.lastPathSegment}")
                    val uploadTask = reference.putFile(new_uri)

                    uploadTask.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
//                            progressDialog.dismiss()
                            progDialog.dismiss()
                            DoneBtn.visibility = View.VISIBLE

                            newImage = it.toString()
                            newImg.load(newImage)
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        addNewImages.setOnClickListener {
//            newImg.visibility = View.VISIBLE
//            DoneBtn.visibility = View.VISIBLE

            firestore.collection("Settings").document("SliderImages").get()
                .addOnSuccessListener {
                    sliderImages = it.get("Images") as ArrayList<String>

                }

            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            resultLauncher.launch(Intent.createChooser(intent, "Select image"))

        }

        DoneBtn.setOnClickListener {
//
//            newImg.visibility = View.INVISIBLE
//            DoneBtn.visibility = View.INVISIBLE

            sliderImages.add(newImage)

            val images = mapOf(
                "Images" to sliderImages
            )

            firestore.collection("Settings").document("SliderImages")
                .set(images).addOnSuccessListener {
                }

            getImages()
        }

        getImages()

    }


    class img(var image: String = "")

//    val progressDialog = ProgressDialog(con).apply {
//        setTitle("Loading")
//        setMessage("Loading")
//        setCancelable(false)
//    }

    private fun getImages() {

        firestore.collection("Settings").document("SliderImages").get()
            .addOnSuccessListener {
                val a = it.get("Images") as ArrayList<String>

                val arr = ArrayList<img>()

                a.forEach {
                    arr.add(img(it))
                }

                val adapter = ImagesRecycleView(this, arr)
                sliderImagesRecycle.adapter = adapter
                sliderImagesRecycle.layoutManager = GridLayoutManager(this, 2)
            }
    }

    class ImagesRecycleView(private val cont: Context, private val List: ArrayList<img>) :
        RecyclerView.Adapter<ViewH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
            return ViewH(
                LayoutInflater.from(cont).inflate(R.layout.main_categories_template, parent, false)
            )

        }

        override fun onBindViewHolder(holder: ViewH, posi: Int) {
            if (List[posi].image.isNotEmpty())
                holder.itemView.cate_img.load(List[posi].image)

            holder.itemView.setOnLongClickListener {
                FirebaseFirestore.getInstance().collection("Settings").document("SliderImages")
                    .get()
                    .addOnSuccessListener {
                        val sliderImages = ArrayList<String>()
                        val a = it.get("Images") as ArrayList<String>
                        a.forEach { currentImg ->
                            if (currentImg != List[posi].image)
                                sliderImages.add(currentImg)
                        }
                        val images = mapOf(
                            "Images" to sliderImages
                        )
                        FirebaseFirestore.getInstance().collection("Settings")
                            .document("SliderImages")
                            .set(images)

                        val activity = cont as? Activity
                        activity?.recreate()
                    }


                return@setOnLongClickListener false
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