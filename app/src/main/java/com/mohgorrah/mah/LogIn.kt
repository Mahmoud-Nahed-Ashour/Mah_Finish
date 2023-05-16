package com.mohgorrah.mah

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.mohgorrah.mah.Admin.Admin
import kotlinx.android.synthetic.main.activity_log_in.*

class logIn : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        val firestore = FirebaseFirestore.getInstance()

        btnLog.setOnClickListener {
            firestore.collection("Settings").get().addOnSuccessListener {

                val email = it.documents[1].get("email")
                val pass = it.documents[1].get("password")

                if (editemail.text.toString() == email && editpassword.text.toString() == pass){
                    startActivity(Intent(this@logIn, Admin::class.java))
                }

            }
        }


    }


}