package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
  var isReady by mutableStateOf(false)
    private set

  var isSpeaking by mutableStateOf(false)
    private set

  var speechRate by mutableFloatStateOf(1.0f)
    private set

  init {
    // Initialized in constructor
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale("tr", "TR"))
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        // Fallback to default locale
        tts?.language = Locale.getDefault()
      }
      tts?.setSpeechRate(speechRate)
      tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
          isSpeaking = true
        }

        override fun onDone(utteranceId: String?) {
          isSpeaking = false
        }

        override fun onError(utteranceId: String?) {
          isSpeaking = false
        }
      })
      isReady = true
    } else {
      isReady = false
    }
  }

  fun setRate(rate: Float) {
    val clamped = rate.coerceIn(0.75f, 1.5f)
    speechRate = clamped
    tts?.setSpeechRate(clamped)
  }

  fun speak(text: String) {
    if (!isReady || tts == null) return
    tts?.stop()
    isSpeaking = true
    tts?.setSpeechRate(speechRate)
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "islamic_reader_utterance")
  }

  fun stop() {
    tts?.stop()
    isSpeaking = false
  }

  fun toggle(text: String) {
    if (isSpeaking) {
      stop()
    } else {
      speak(text)
    }
  }

  fun release() {
    stop()
    tts?.shutdown()
    tts = null
    isReady = false
    isSpeaking = false
  }
}
