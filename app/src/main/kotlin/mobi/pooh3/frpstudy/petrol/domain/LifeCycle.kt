package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.rx.unOptional
import java.util.*

class LiveCycle(sNozzle1: Observable<UpDown>, sNozzle2: Observable<UpDown>, sNozzle3: Observable<UpDown>) {
    val sStart: Observable<Fuel>
    val sEnd: Observable<End>
    val fillActive: BehaviorSubject<Optional<Fuel>>

    init {
        val sLiftNozzle =
                Observable.merge(
                        whenLifted(sNozzle1, Fuel.ONE),
                        whenLifted(sNozzle2, Fuel.TWO),
                        whenLifted(sNozzle3, Fuel.THREE)
                )

        this.fillActive = BehaviorSubject.create()

        this.sStart =
                sLiftNozzle
                        .withLatestFrom(fillActive, { newFuel, fillActive_ ->
                            if (fillActive_ == null)
                                Optional.of(newFuel)
                            else
                                Optional.empty()
                        }
                        )
                        .unOptional()

        this.sEnd =
                Observable.merge(
                        whenSetDown(sNozzle1, Fuel.ONE, fillActive),
                        whenSetDown(sNozzle2, Fuel.TWO, fillActive),
                        whenSetDown(sNozzle3, Fuel.THREE, fillActive)
                )

        this.fillActive.loop(
                Observable
                        .merge(
                                sEnd.map   { Optional.empty<Fuel>() },
                                sStart.map { Optional.of(it) }
                        )
                        .hold(Optional.empty())
        )
    }
}

fun whenLifted(sNozzle: Observable<UpDown>, nozzleFuel: Fuel): Observable<Fuel> =
        sNozzle
                .filter { u -> u == UpDown.UP }
                .map { nozzleFuel }

fun whenSetDown(sNozzle: Observable<UpDown>, nozzleFuel: Fuel, fillActive: BehaviorSubject<Optional<Fuel>>): Observable<End> =
        sNozzle
                .withLatestFrom(fillActive, { u: UpDown, f: Optional<Fuel> ->
                    if (u == UpDown.DOWN && f == Optional.of(nozzleFuel))
                        Optional.of(End.END)
                    else
                        Optional.empty()
                }
                )
                .unOptional()