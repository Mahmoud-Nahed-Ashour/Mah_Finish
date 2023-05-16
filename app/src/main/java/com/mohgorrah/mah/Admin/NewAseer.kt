package com.mohgorrah.mah.Admin

import android.app.Activity
import android.app.DatePickerDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_new_aseer.*
import kotlinx.android.synthetic.main.activity_photos_posts.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class NewAseer : AppCompatActivity() {

    var image = ""
    lateinit var progDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aseer)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

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
                            progDialog.dismiss()
                            image = it.toString()
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        btnAdd.setOnClickListener {
            addAseer()
        }

        imagebtn.setOnClickListener {
            val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            resultLauncher.launch(Intent.createChooser(intent, "Select image"))
        }

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }
        tvDatePicker.setOnClickListener {
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }

    private fun addAseer() {
        if (name.text.toString().isNotEmpty() && tvDatePicker.text.toString()
                .isNotEmpty() && location.text.toString().isNotEmpty() && hkm.text.toString()
                .isNotEmpty()
        ) {
            val post = hashMapOf(
                "id" to UUID.randomUUID().toString(),
                "name" to name.text.toString(),
                "moabd" to moabd.isChecked,
                "edare" to edare.isChecked,
                "male" to male.isChecked,
                "date" to tvDatePicker.text.toString(),
                "image" to image,
                "location" to location.text.toString(),
                "hkm" to hkm.text.toString().toInt(),
            )

            Firebase.firestore.collection("Aseer").add(post).addOnSuccessListener {
                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
//        startActivity(Intent(this, Container::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLable(myCalendar: Calendar) {

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        tvDatePicker.text = sdf.format(myCalendar.time)
    }

    private fun getFile(context: Context, uri: Uri): File {
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