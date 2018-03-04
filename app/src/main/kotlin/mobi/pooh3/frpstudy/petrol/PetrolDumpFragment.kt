package mobi.pooh3.frpstudy.petrol

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import android.media.ToneGenerator
import android.media.AudioManager
import io.reactivex.rxkotlin.withLatestFrom
import mobi.pooh3.frpstudy.R
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.petrol.domain.End
import mobi.pooh3.frpstudy.petrol.domain.Fuel
import mobi.pooh3.frpstudy.petrol.domain.UpDown
import mobi.pooh3.frpstudy.rx.unOptional
import java.util.*




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


