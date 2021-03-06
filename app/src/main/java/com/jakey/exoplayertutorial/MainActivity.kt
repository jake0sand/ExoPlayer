package com.jakey.exoplayertutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.jakey.exoplayertutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Player.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var player: ExoPlayer
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        getLiveData()
    }

    override fun onStart() {
        super.onStart()

        setupPlayer()
        addMp3Files()
        addMp4Files()
        player.seekTo(
            viewModel.restoreMediaLiveData.value ?: 0,
            viewModel.seekTimeLiveData.value ?: 0
        )
        player.play()
    }



    fun getLiveData() {

        viewModel.seekTimeLiveData.observe(this, Observer {
            viewModel.setSeekTimeLiveData(player.currentPosition)
        })
        viewModel.restoreMediaLiveData.observe(this, Observer {
            viewModel.setRestoreMediaLiveData(player.currentMediaItemIndex)
        })

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
        getLiveData()
        player.release()
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)

        binding.title.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "title not found"
    }
}