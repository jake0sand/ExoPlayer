package com.jakey.exoplayertutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.jakey.exoplayertutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var player: ExoPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.progressBar = findViewById(R.id.progressBar)
//        titleTv = findViewById(R.id.title)

        setupPlayer()
        addMp3Files()
        addMp4Files()

        if (savedInstanceState != null) {
            savedInstanceState.getInt("MediaItem").let { restoredMedia ->
                val seekTime  = savedInstanceState.getLong("SeekTime")
                player.seekTo(restoredMedia, seekTime)
                player.play()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong("SeekTime", player.currentPosition)
        outState.putInt("MediaItem", player.currentMediaItemIndex)
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        player.addListener(this)
    }

    private fun addMp4Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    private fun addMp3Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.test_mp3))
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_BUFFERING -> binding.progressBar.visibility = View.VISIBLE
            Player.STATE_READY -> binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)

        binding.title.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "title not found"
    }
}