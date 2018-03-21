package mobi.pooh3.frpstudy.metronome

import android.media.AudioManager
import android.media.ToneGenerator

object ToneGeneratorHolder {
    private val toneGenerator: ToneGenerator by lazy { ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME) }
    fun instance() = toneGenerator
}