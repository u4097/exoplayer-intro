/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.example.exoplayer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*


/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            Log.d("EXOPLAYER INIT ","ON START")
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23)
            Log.d("EXOPLAYER INIT ","ON RESUME")
            initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            Log.d("EXOPLAYER RELEASE ","ON PAUSE")
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            Log.d("EXOPLAYER RELEASE ","ON STOP")
            releasePlayer()
        }
    }


    private fun initializePlayer() {
        if (player == null) {
            Log.d("EXOPLAYER","IS NULL - NOW IS INIT")

            player = ExoPlayerFactory.newSimpleInstance(
                    this,
                    DefaultRenderersFactory(this),
                    DefaultTrackSelector())
            video_view.player = player
            player?.playWhenReady = playWhenReady
            player?.seekTo(currentWindow, playbackPosition)
        }

        val uri = Uri.parse(getString(R.string.media_url_mp3))
        val mediaSource = buildMediaSource(uri)
        player?.prepare(mediaSource, true, false)
    }


    private fun releasePlayer() {
        if (this::player != null) {
            playbackPosition = player?.currentPosition ?: 0L
            currentWindow = player?.currentWindowIndex ?: 0
            playWhenReady = player?.playWhenReady ?: false
            player?.release()
            player = null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource =
            ExtractorMediaSource.Factory(
                    DefaultHttpDataSourceFactory("exoplayer-codelab"))
                    .createMediaSource(uri)

    @SuppressLint("InlinedApi")
    fun hideSystemUi() {
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
