package com.mohgorrah.mah.User

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mohgorrah.mah.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_livres_pdfactivity.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream


class LivresPDFActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livres_pdfactivity)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

//        pdfview.fromAsset("storypdf.pdf").load()

        val url  = intent.getStringExtra("url")

        val downloadTask = DownloadTask(object : DownloadTask.DownloadListener {
            override fun onDownloaded(inputStream: InputStream) {
                pdfview.fromStream(inputStream).load()
            }

            override fun onFailed(exception: Exception) {
                exception.printStackTrace()
            }
        })

        downloadTask.execute(url)





//        pdfview.settings.javaScriptEnabled = true
//        pdfview.loadUrl("https://docs.google.com/viewer?url=$url")


        backTvL.setOnClickListener {
            finish()
        }
    }



    private class DownloadTask(val listener: DownloadListener) :
        AsyncTask<String, Void, InputStream>() {

        interface DownloadListener {
            fun onDownloaded(inputStream: InputStream)
            fun onFailed(exception: Exception)
        }

        override fun doInBackground(vararg params: String?): InputStream? {
            val url = params[0] ?: return null

            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                return response.body?.byteStream()
            } catch (e: Exception) {
                listener.onFailed(e)
            }

            return null
        }

        override fun onPostExecute(result: InputStream?) {
            super.onPostExecute(result)

            if (result != null) {
                listener.onDownloaded(result)
            }
        }
    }

}