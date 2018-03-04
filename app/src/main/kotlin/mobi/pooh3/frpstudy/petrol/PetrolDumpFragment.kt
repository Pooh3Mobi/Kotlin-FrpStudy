package mobi.pooh3.frpstudy.petrol

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.media.ToneGenerator
import android.media.AudioManager
import mobi.pooh3.frpstudy.R

fun beep() {
    val toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME/2)
    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP)
}


class PetrolDumpFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_petrol_dump, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)
    }


    companion object {
        fun newInstance(): PetrolDumpFragment {
            return PetrolDumpFragment()
        }
    }

}


