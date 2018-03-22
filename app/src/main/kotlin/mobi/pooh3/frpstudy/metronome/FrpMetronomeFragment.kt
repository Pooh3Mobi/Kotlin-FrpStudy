package mobi.pooh3.frpstudy.metronome

import android.media.ToneGenerator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.userChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_frp_metronome.*
import mobi.pooh3.frpstudy.R
import mobi.pooh3.frpstudy.share.DoOnEachLifecycleObserver
import java.util.concurrent.TimeUnit


class FrpMetronomeFragment : Fragment() {
    private val disposables: CompositeDisposable = CompositeDisposable()
    override fun onCreateView(inf: LayoutInflater?, ctr: ViewGroup?, savedInstanceState: Bundle?): View?
            = inf!!.inflate(R.layout.fragment_frp_metronome, ctr, false)

    override fun onViewCreated(v: View?, bdl: Bundle?) {
        val sProgress = seekBar.userChanges()
                .map { if (it <= 50) 50 else it }.share()
        val sToggle = toggleButton.checkedChanges().share()

        // inputs to outputs
        val outputs = SimpleMetronome().create(Inputs(
                seekBar.progress.toLong(),
                sProgress, sProgress.debounce(1200, TimeUnit.MILLISECONDS),
                sToggle
        ))

        disposables.addAll(
                img_pulse.pulse(outputs.duration),
                text_bpm.text(outputs.bpmText)
        )

        lifecycle.addObserver(
                DoOnEachLifecycleObserver()
                        .doOnStop { if (!disposables.isDisposed) disposables.dispose() }
                        .doOnDestroy { if (!disposables.isDisposed) disposables.dispose() }
        )

    }

    private fun  ImageView.pulse(observable: Observable<out Long>) = observable.subscribe(this.pulse())
    private fun ImageView.pulse(): (Long) -> Unit = { newDuration ->
        fun View.fadeOut(from: Float = 1f, to: Float = 0f, newDuration: Long) = this
                .apply { this.alpha = from }
                .animate()
                .apply { duration = newDuration }
                .alpha(to)

        this.fadeOut(newDuration = newDuration)
        ToneGeneratorHolder.instance()
                .startTone(ToneGenerator.TONE_PROP_PROMPT)
    }


    companion object {
        fun newInstance(): FrpMetronomeFragment = FrpMetronomeFragment()
    }
}

fun  TextView.text(observable: Observable<out CharSequence>) = observable.subscribe(this.text())!!
