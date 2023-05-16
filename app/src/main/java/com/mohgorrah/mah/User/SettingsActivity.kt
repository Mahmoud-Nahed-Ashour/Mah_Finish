package com.mohgorrah.mah.User

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mohgorrah.mah.Admin.Admin
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.dialog_admin.*


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )
        backS.setOnClickListener {
            finish()
        }

        whoAreWe.setOnClickListener {
            startActivity(
                Intent(this@SettingsActivity, WhoAreWeActivity::class.java)
            )
        }

        adminbtn.setOnClickListener {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_admin)
            dialog.show()


            Firebase.firestore.collection("Settings").document("Admin").get()
                .addOnSuccessListener {

                    val email = it.get("email")
                    val pass = it.get("password")

                    dialog.next.setOnClickListener {
                        if (dialog.emailE.text.toString() == email && dialog.passE.text.toString() == pass) {
                            dialog.dismiss()
                            startActivity(
                                Intent(this@SettingsActivity, Admin::class.java)
                            )
                        } else {
                            Toast.makeText(
                                this@SettingsActivity,
                                "خطأ في البيانات",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }


        }
        location.setOnClickListener {
            startActivity(
                Intent(this@SettingsActivity, MapLocationsActivity::class.java)
            )
        }

    }
}