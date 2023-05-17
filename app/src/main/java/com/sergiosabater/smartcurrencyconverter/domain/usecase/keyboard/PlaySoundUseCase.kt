package com.sergiosabater.smartcurrencyconverter.domain.usecase.keyboard

import android.content.Context
import android.media.MediaPlayer
import com.sergiosabater.smartcurrencyconverter.R
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.BACKSPACE_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.CLEAR_BUTTON_STRING


class PlaySoundUseCase(private val context: Context) {

    fun play(keyText: String) {
        val soundResId = when (keyText) {
            CLEAR_BUTTON_STRING, BACKSPACE_SYMBOL_STRING -> R.raw.back_clic_sound
            else -> R.raw.clic_sound
        }

        val mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
        mediaPlayer.start()
    }
}

