package com.mohgorrah.mah.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_settings.*

class WhoAreWeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_who_are_we)


        backS.setOnClickListener {
            finish()
        }
    }
}