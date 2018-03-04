package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import java.util.*


class NotifyPointOfSale(lc: LifeCycle, sClearSale: Observable<Unit>, fi: Fill) {
    val sStart: Observable<Fuel>
    val fillActive: BehaviorSubject<Optional<Fuel>>
    val fuelFlowing: BehaviorSubject<Optional<Fuel>>
    val sEnd: Observable<End>
    val sBeep: Observable<Unit>
    val sSaleComplete: Observable<Sale>

    init {
        val phase: BehaviorSubject<Phase> = BehaviorSubject.create()
        this.sStart = lc.sStart.withLatestFrom(phase, { f, p -> (f to p)})
                .filter { (_, p) -> p == Phase.IDLE }
                .map { (f, _) -> f }
    }
}

