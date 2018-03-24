package mobi.pooh3.frpstudy.metronome

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


interface Metronome { fun create(inputs: Inputs): Outputs }

data class Inputs(
        val defaultDuration: Long,
        val sProgress: Observable<Int>,
        val sToggle: Observable<Boolean>
)
data class Outputs(
        val duration: Observable<Long> = BehaviorSubject.createDefault(0L),
        val bpmText: Observable<String> = BehaviorSubject.createDefault("")
)
class SimpleMetronome : Metronome {
    override fun create(inputs: Inputs): Outputs {

        val pl = Pulse(
                inputs.sProgress,
                inputs.defaultDuration,
                inputs.sToggle)

        val bpm = Bpm(
                inputs.sProgress,
                inputs.defaultDuration)

        return Outputs(
                duration = pl.duration,
                bpmText = bpm.bpmText.map { Formatters().formatBpmInfo(it) }
        )
    }
}
class DebouncedMetronome : Metronome {
    override fun create(inputs: Inputs): Outputs {

        val pl = Pulse(
                inputs.sProgress.debounce(1200, TimeUnit.MILLISECONDS),
                inputs.defaultDuration,
                inputs.sToggle)

        val bpm = Bpm(
                inputs.sProgress,
                inputs.defaultDuration)

        return Outputs(
                duration = pl.duration,
                bpmText = bpm.bpmText.map { Formatters().formatBpmInfo(it) }
        )
    }
}
class Pulse(sProgress: Observable<Int>, defaultDuration: Long, sToggle: Observable<Boolean>) {
    val duration: Observable<Long>
    init {
        val toggleValue = BehaviorSubject.createDefault(false)
        val durationValue = BehaviorSubject.createDefault(defaultDuration)
        duration = durationValue
                .switchInterval()
        sProgress
                .map { it.toLong() }
                .subscribe(durationValue)
        sToggle.subscribe(toggleValue)
    }

    private fun Observable<Long>.switchInterval(): Observable<Long> =
            this.switchMap {
                Observable.interval(it, TimeUnit.MILLISECONDS)
                        .withLatestFrom(this, {_, duration_ -> duration_})
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


