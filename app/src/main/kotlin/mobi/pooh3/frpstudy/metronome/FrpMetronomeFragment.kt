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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_frp_metronome.*
import mobi.pooh3.frpstudy.R
import mobi.pooh3.frpstudy.extensions.toBehaviorSubject
import mobi.pooh3.frpstudy.share.DoOnEachLifecycleObserver


class FrpMetronomeFragment : Fragment() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inf: LayoutInflater, ctr: ViewGroup?, savedInstanceState: Bundle?): View?
            = inf.inflate(R.layout.fragment_frp_metronome, ctr, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sProgress = seekBar.userChanges().toBehaviorSubject(seekBar.progress)
        val sToggle = toggleButton.checkedChanges().toBehaviorSubject(toggleButton.isChecked)

        val sToggledMetronome = sToggle.map {
            if (it) DebouncedMetronome()
            else SimpleMetronome()
        }

        // inputs to outputs
        val outputs = sToggledMetronome.map{ metronome -> metronome.create(Inputs(
                seekBar.progress.toLong(),
                sProgress,
                sToggle
        ))}

        val duration = Observable.switchOnNext(outputs.map { o -> o.duration })
        val bpmText  = Observable.switchOnNext(outputs.map { o -> o.bpmText })

        disposables.addAll(
                img_pulse.pulse(duration),
                text_bpm.text(bpmText)
        )

        lifecycle.addObserver(
                DoOnEachLifecycleObserver()
                        .doOnStop { if (!disposables.isDisposed) disposables.dispose() }
                        .doOnDestroy { if (!disposables.isDisposed) disposables.dispose() }
        )
    }

    private fun  ImageView.pulse(observable: Observable<out Long>) =
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(this.pulse())
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

fun  TextView.text(observable: Observable<out CharSequence>) =
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(this.text())!!
