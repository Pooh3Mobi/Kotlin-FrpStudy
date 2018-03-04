package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.rx.combineLatest
import java.util.*


data class Inputs(
        val sNozzle1: Observable<UpDown>,
        val sNozzle2: Observable<UpDown>,
        val sNozzle3: Observable<UpDown>,
        val sKeypad: Observable<Key>,
        val sFuelPluses: Observable<Int>,
        val calibration: BehaviorSubject<Double>,
        val price1: BehaviorSubject<Double>,
        val price2: BehaviorSubject<Double>,
        val price3: BehaviorSubject<Double>,
        val sClearSale: Observable<Unit>
)

data class Outputs(
        val delivery: Observable<Delivery> = BehaviorSubject.createDefault(Delivery.OFF),
        val presetLCD: Observable<String> = BehaviorSubject.createDefault(""),
        val saleCostLCD: Observable<String> = BehaviorSubject.createDefault(""),
        val saleQuantityLCD: Observable<String> = BehaviorSubject.createDefault(""),
        val priceLCD1: Observable<String> = BehaviorSubject.createDefault(""),
        val priceLCD2: Observable<String> = BehaviorSubject.createDefault(""),
        val priceLCD3: Observable<String> = BehaviorSubject.createDefault(""),
        val sBeep: Observable<Unit> = Observable.empty(),
        val sSaleComplete: Observable<Sale> = Observable.empty()
)

interface Pump {
    fun create(inputs: Inputs): Outputs
}


class LifeCyclePump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val lc = LiveCycle(
                inputs.sNozzle1,
                inputs.sNozzle2,
                inputs.sNozzle3
        )

        return Outputs()
                .copy(
                        delivery = lc.fillActive.map {
                            when(it) {
                                Optional.of(Fuel.ONE)   -> Delivery.FAST1
                                Optional.of(Fuel.TWO)   -> Delivery.FAST2
                                Optional.of(Fuel.THREE) -> Delivery.FAST3
                                else -> Delivery.OFF
                            }}
                )
                .copy(
                        saleQuantityLCD = lc.fillActive.map {
                            when(it) {
                                Optional.of(Fuel.ONE)   -> "1"
                                Optional.of(Fuel.TWO)   -> "2"
                                Optional.of(Fuel.THREE) -> "3"
                                else -> ""
                            }}
                )
    }
}


class AccumulatePulsesPump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val lc = LiveCycle(
                inputs.sNozzle1,
                inputs.sNozzle2,
                inputs.sNozzle3
        )
        val litersDelivered = accumulate(
                lc.sStart.flatMap { Observable.empty<Unit>() },
                inputs.sFuelPluses,
                inputs.calibration
                )
        return Outputs()
                .copy(
                        delivery = lc.fillActive.map {
                            when(it) {
                                Optional.of(Fuel.ONE)   -> Delivery.FAST1
                                Optional.of(Fuel.TWO)   -> Delivery.FAST2
                                Optional.of(Fuel.THREE) -> Delivery.FAST3
                                else -> Delivery.OFF
                            }}
                )
                .copy(
                        saleCostLCD = litersDelivered.map {  q -> Formatters.formatSaleQuantity(q) }
                )
    }

    companion object {
        fun accumulate(
                sClearAccumulator: Observable<Unit>,
                sPulses: Observable<Int>,
                calibration: BehaviorSubject<Double>): Observable<Double> {

            val total: BehaviorSubject<Int> = BehaviorSubject.create()
            total.loop(
                    Observable.merge(
                            sClearAccumulator.map { 0 },
                            sPulses.withLatestFrom(total,
                                    { pulses_, total_ -> pulses_ + total_ })
                    ).hold(0))
            return total.combineLatest(calibration, { total_, calibration_ -> total_ * calibration_ })
        }
    }

}



