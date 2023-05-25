package com.sergiosabater.smartcurrencyconverter.util.sound

import android.content.Context
import android.media.MediaPlayer

class SoundPlayerImpl(private val context: Context) : SoundPlayer {
    override fun playSound(resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
}