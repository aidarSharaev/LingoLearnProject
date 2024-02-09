package ru.aidar.lingolearn.component.text_to_speech

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.aidar.lingolearn.utils.AvailableLanguage
import java.util.Locale
import javax.inject.Inject

class LlTextToSpeech @Inject constructor(
    @ApplicationContext val context: Context
) {
    private var llTextToSpeech: TextToSpeech? = null

    fun readTheText(textToSpeak: String, language: AvailableLanguage) {
        llTextToSpeech = TextToSpeech(context) {
            if(it == TextToSpeech.SUCCESS) {
                llTextToSpeech?.let { textToSpeech ->
                    textToSpeech.language = Locale(language.language)
                    textToSpeech.setSpeechRate(1.0f)
                    textToSpeech.speak(
                        textToSpeak,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }

}