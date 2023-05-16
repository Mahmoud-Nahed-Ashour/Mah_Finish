package com.mohgorrah.mah.User

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mohgorrah.mah.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        //sharedPref = AppControllers.getInstance().getSharedPreferences("mode", Context.MODE_PRIVATE);
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()

//        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
//        finish()
    }
}