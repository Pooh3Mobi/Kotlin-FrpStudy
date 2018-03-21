package mobi.pooh3.frpstudy.metronome

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.rx.unOptional
import java.util.*
import java.util.concurrent.TimeUnit

data class Inputs(val defaultDuration: Long, val sProgress: Observable<Int>, val sDebouncedProgress: Observable<Int>, val sToggle: Observable<Boolean>)
data class Outputs(
        val duration: Observable<Long> = BehaviorSubject.createDefault(0L),
        val bpmText: Observable<String> = BehaviorSubject.createDefault("")
)
class Metronome {
    fun create(inputs: Inputs): Outputs {

        val mn = Pulse(
                inputs.sProgress,
                inputs.sDebouncedProgress,
                inputs.defaultDuration,
                inputs.sToggle)

        val bpm = Bpm(
                inputs.sProgress,
                inputs.defaultDuration)

        return Outputs(
                duration = mn.duration,
                bpmText = bpm.bpmText.map { Formatters().formatBpmInfo(it) }
        )
    }
}
class Pulse(sProgress: Observable<Int>, sDebouncedProgress: Observable<Int>, defaultDuration: Long, sToggle: Observable<Boolean>) {
    val duration: Observable<Long>
    init {

        val toggleValue = BehaviorSubject.createDefault(false)

        val sToggledProgress =
                Observable.merge(
                        whenToggleOn(toggleValue, sDebouncedProgress),
                        whenToggleOff(toggleValue, sProgress)
                )

        val durationValue = BehaviorSubject.createDefault(defaultDuration)

        duration = durationValue
                .switchInterval()

        sToggledProgress
                .map { it.toLong() }
                .subscribe(durationValue)

        sToggle.subscribe(toggleValue)

    }

    private fun whenToggleOff(toggleValue: Observable<Boolean>, sProgress: Observable<Int>) =
            sProgress.withLatestFrom(toggleValue, { progress, toggle ->
                if (toggle) Optional.empty()
                else Optional.of(progress)
            }).unOptional()

    private fun whenToggleOn(toggleValue: Observable<Boolean>, sProgress: Observable<Int>) =
            sProgress.withLatestFrom(toggleValue, { progress, toggle ->
                if (toggle) Optional.of(progress)
                else Optional.empty()
            }).unOptional()

    private fun Observable<Long>.switchInterval(): Observable<Long> =
            this.switchMap {
                Observable.interval(it, TimeUnit.MILLISECONDS)
                        .withLatestFrom(this, {_, duration_ -> duration_})
                        .observeOn(AndroidSchedulers.mainThread())
            }
}
class Bpm(sProgress: Observable<Int>, defaultDuration: Long) {
    val bpmText: Observable<Long>
    init {
        val bpmTextValue = BehaviorSubject.createDefault(defaultDuration)
        bpmText = bpmTextValue
        sProgress.map { it.toLong() }.subscribe(bpmTextValue)
    }
}


