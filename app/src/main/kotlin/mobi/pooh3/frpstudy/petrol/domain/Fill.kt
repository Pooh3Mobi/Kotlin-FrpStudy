package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.rx.combineLatest
import mobi.pooh3.frpstudy.extensions.rx.unOptional
import java.util.*


class Fill(
        sClearAccumulator: Observable<Unit>, sFuelPulses: Observable<Int>,
        calibration: BehaviorSubject<Double>, price1: BehaviorSubject<Double>,
        price2: BehaviorSubject<Double>, price3: BehaviorSubject<Double>,
        sStart: Observable<Fuel>
        ) {
    val price: Observable<Double> = capturePrice(sStart, price1, price2, price3)
    val litersDelivered: Observable<Double> = AccumulatePulsesPump.accumulate(sClearAccumulator, sFuelPulses, calibration)
    val dollersDelivered: Observable<Double> = litersDelivered.combineLatest(price, { liters, price_ -> liters * price_ })

    companion object {
        fun capturePrice(sStart: Observable<Fuel>,
                         price1: BehaviorSubject<Double>,
                         price2: BehaviorSubject<Double>,
                         price3: BehaviorSubject<Double>): Observable<Double> {

            val sPrice: Observable<Fuel>.(BehaviorSubject<Double>, Fuel) -> Observable<Double> = { price, fuel ->
                this
                        .withLatestFrom(price, { f, p ->
                            if(f == fuel)
                                Optional.of(p)
                            else
                                Optional.empty() }
                        )
                        .unOptional()
            }

            val sPrice1: Observable<Double> = sPrice(sStart, price1, Fuel.ONE)
            val sPrice2: Observable<Double> = sPrice(sStart, price2, Fuel.TWO)
            val sPrice3: Observable<Double> = sPrice(sStart, price3, Fuel.THREE)
            return Observable.merge(
                    sPrice1, sPrice2, sPrice3
            ).hold(0.0)
        }
    }

}