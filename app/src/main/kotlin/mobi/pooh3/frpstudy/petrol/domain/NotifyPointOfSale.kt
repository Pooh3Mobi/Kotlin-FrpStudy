package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.*
import mobi.pooh3.frpstudy.extensions.rx.combineLatest
import mobi.pooh3.frpstudy.extensions.rx.unOptional
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
        // IDLE -> start
        sStart = lc.sStart.gate(phase.map { p -> p == Phase.IDLE })
        // FILLING -> end
        sEnd = lc.sEnd.gate(phase.map { p -> p == Phase.FILLING })
        phase.loop(
                Observable.merge(
                        sStart.map { Phase.FILLING },
                        sEnd.map   { Phase.POS },
                        sClearSale.map { Phase.IDLE }
                ).hold(Phase.IDLE)
        )

        fuelFlowing =
                Observable.merge(
                        sStart.map { f -> f.toOptional() },
                        sEnd.map   { Optional.empty<Fuel>() }
                ).hold(Optional.empty())

        fillActive =
                Observable.merge(
                        sStart.map { f -> f.toOptional() },
                        sClearSale.map { Optional.empty<Fuel>() }
                ).hold(Optional.empty())

        sBeep = sClearSale
        sSaleComplete = sEnd.snapshot(
                fuelFlowing.combineLatest(fi.price, fi.dollersDelivered, fi.litersDelivered,
                        { oFuel, price_, dollers, liters ->
                            if (oFuel.isPresent) {
                                Sale(oFuel.get(), price_, dollers, liters).toOptional()
                            } else {
                                Optional.empty()
                            }
                        })
        ).unOptional()
    }
}


