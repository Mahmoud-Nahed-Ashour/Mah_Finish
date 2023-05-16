package com.mohgorrah.mah.User

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.mohgorrah.mah.R

class PreviewVideo : AppCompatActivity() {

    private var mPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView

    private lateinit var videoURL:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_view)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.orange)))

        videoURL = intent.getStringExtra("video").toString()

        playerView = findViewById(R.id.playerView)

    }


    private fun initPlayer() {

        // Create a player instance.
        mPlayer = SimpleExoPlayer.Builder(this).build()

        // Bind the player to the view.
        playerView.player = mPlayer

        //setting exoplayer when it is ready.
        mPlayer!!.playWhenReady = true

        // Set the media source to be played.
        mPlayer!!.setMediaSource(buildMediaSource())

        // Prepare the player.
        mPlayer!!.prepare()

    }

    override fun onStart() {
        super.onStart()
            initPlayer()

    }

    override fun onResume() {
        super.onResume()
        if (mPlayer == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
            releasePlayer()

    }

    override fun onStop() {
        super.onStop()
            releasePlayer()

    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        //release player when done
        mPlayer!!.release()
        mPlayer = null
    }

    //creating mediaSource
    private fun buildMediaSource(): MediaSource {

        // Create a data source factory.
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        // Create a progressive media source pointing to a stream uri.

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoURL))

        return mediaSource
    }



}