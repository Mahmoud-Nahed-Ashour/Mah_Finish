package com.mohgorrah.mah.User

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mohgorrah.mah.R
import kotlinx.android.synthetic.main.activity_total_horia.*

class TotalHoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_horia)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        backTv.setOnClickListener {
            finish()
        }

        button1.setOnClickListener {
            startActivity(Intent(this, allAseer::class.java))
        }

        button2.setOnClickListener {
            val i = Intent(this, aseer__::class.java)
            i.putExtra("type", "moabd")
            startActivity(i)
        }

        button3.setOnClickListener {
            val i = Intent(this, aseer____::class.java)
            i.putExtra("type", "new")
            startActivity(i)
        }

        button4.setOnClickListener {
            val i = Intent(this, aseer____::class.java)
            i.putExtra("type", "current")
            startActivity(i)
        }
        button5.setOnClickListener {
            val i = Intent(this, aseer__::class.java)
            i.putExtra("type", "20")
            startActivity(i)
        }

        button6.setOnClickListener {
            val i = Intent(this, aseer__::class.java)
            i.putExtra("type", "35")
            startActivity(i)
        }
        button7.setOnClickListener {
            val i = Intent(this, aseer__::class.java)
            i.putExtra("type", "edare")
            startActivity(i)
        }
        button8.setOnClickListener {
            val i = Intent(this, aseer__::class.java)
            i.putExtra("type", "male")
            startActivity(i)
        }
    }
}