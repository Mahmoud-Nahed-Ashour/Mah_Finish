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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_new_book.*
import kotlinx.android.synthetic.main.activity_photos_posts.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class NewBook : AppCompatActivity() {

    var newBook = ""
    lateinit var progDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_book)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        progDialog = ProgressDialog(this)
        progDialog.setTitle("جاري التحميل")
        progDialog.setMessage("جاري رفع الملف...")
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
                        Firebase.storage.reference.child("Books/${new_uri.lastPathSegment}")
                    val uploadTask = reference.putFile(new_uri)

                    uploadTask.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            progDialog.dismiss()

                            newBook = it.toString()

                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        uploadbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                .setType("application/pdf")
            resultLauncher.launch(Intent.createChooser(intent, "Select Book"))
        }


        Donebtn.setOnClickListener {
            UploadBook()
        }

    }

    private fun UploadBook() {

        if (name.text.toString().isNotEmpty() && author.text.toString().isNotEmpty()) {
            if (newBook != "") {
                val book = Book(
                    UUID.randomUUID().toString(),
                    name.text.toString(),
                    author.text.toString(),
                    newBook
                )

                Firebase.firestore.collection("Books").add(book).addOnSuccessListener {
                    Toast.makeText(this@NewBook, "Uploaded", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "no book", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename =
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