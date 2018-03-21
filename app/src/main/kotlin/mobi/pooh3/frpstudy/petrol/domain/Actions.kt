package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.extensions.rx.combineLatest
import mobi.pooh3.frpstudy.extensions.toOptional
import java.util.*

class LifeCyclePump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val lc = LifeCycle(
                inputs.sNozzle1,
                inputs.sNozzle2,
                inputs.sNozzle3
        )

        return Outputs(
                delivery = lc.fillActive.map {
                    when(it) {
                        Fuel.ONE.toOptional()   -> Delivery.FAST1
                        Fuel.TWO.toOptional()   -> Delivery.FAST2
                        Fuel.THREE.toOptional() -> Delivery.FAST3
                        else -> Delivery.OFF
                    }},
                saleQuantityLCD = lc.fillActive.map {
                    when(it) {
                        Fuel.ONE.toOptional()   -> "1"
                        Fuel.TWO.toOptional()   -> "2"
                        Fuel.THREE.toOptional() -> "3"
                        else -> ""
                    }}
                )
    }
}

class AccumulatePulsesPump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val lc = LifeCycle(
                inputs.sNozzle1,
                inputs.sNozzle2,
                inputs.sNozzle3
        )
        val litersDelivered = accumulate(
                lc.sStart.flatMap { Observable.empty<Unit>() },
                inputs.sFuelPluses,
                inputs.calibration
        )
        return Outputs(
                delivery = lc.fillActive.map {
                    when(it) {
                        Fuel.ONE.toOptional()   -> Delivery.FAST1
                        Fuel.TWO.toOptional()   -> Delivery.FAST2
                        Fuel.THREE.toOptional() -> Delivery.FAST3
                        else -> Delivery.OFF
                    }},
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

class ShowDollersPump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val lc = LifeCycle(
                inputs.sNozzle1,
                inputs.sNozzle2,
                inputs.sNozzle3
        )
        val fi = Fill(
                lc.sStart.flatMap { Observable.empty<Unit>() },
                inputs.sFuelPluses, inputs.calibration,
                inputs.price1, inputs.price2, inputs.price3,
                lc.sStart
        )

        return Outputs(
                delivery = lc.fillActive.map {
                            when(it) {
                                Fuel.ONE.toOptional()   -> Delivery.FAST1
                                Fuel.TWO.toOptional()   -> Delivery.FAST2
                                Fuel.THREE.toOptional() -> Delivery.FAST3
                                else -> Delivery.OFF
                            }
                        },
                saleCostLCD = fi.dollersDelivered.map { q -> Formatters.formatSaleCost(q) },
                saleQuantityLCD = fi.dollersDelivered.map { q -> Formatters.formatSaleQuantity(q) },
                priceLCD1 = priceLCD(lc.fillActive, fi.price, Fuel.ONE, inputs),
                priceLCD2 = priceLCD(lc.fillActive, fi.price, Fuel.TWO, inputs),
                priceLCD3 = priceLCD(lc.fillActive, fi.price, Fuel.THREE, inputs)
        )
    }

    companion object {
        fun priceLCD(fillActive: Observable<Optional<Fuel>>, fillPrice: Observable<Double>, fuel: Fuel, inputs: Inputs): Observable<String> {
            val idlePrice: Observable<Double> = when(fuel) {
                Fuel.ONE -> inputs.price1
                Fuel.TWO -> inputs.price2
                Fuel.THREE -> inputs.price3
            }
            return fillActive.combineLatest(fillPrice, idlePrice,
                    { oFuelSelected, fillPrice_, idlePrice_ ->
                        if (oFuelSelected.isPresent)
                            if (oFuelSelected.get() == fuel)
                                Formatters.formatPrice(fillPrice_)
                            else
                                ""
                        else Formatters.formatPrice(idlePrice_)
                    })
        }
    }
}

class ClearSale : Pump {
    override fun create(inputs: Inputs): Outputs {
        val sStart = BehaviorSubject.create<Fuel>()

        val fi = Fill(
                inputs.sClearSale.flatMap { Observable.empty<Unit>() },
                inputs.sFuelPluses, inputs.calibration,
                inputs.price1, inputs.price2, inputs.price3,
                sStart
        )

        val np = NotifyPointOfSale(
                LifeCycle(
                        inputs.sNozzle1,
                        inputs.sNozzle2,
                        inputs.sNozzle3),
                inputs.sClearSale,
                fi)

        sStart.loop(np.sStart)

        return Outputs(
                delivery = np.fuelFlowing.map {
                    when(it) {
                        Fuel.ONE.toOptional()   -> Delivery.FAST1
                        Fuel.TWO.toOptional()   -> Delivery.FAST2
                        Fuel.THREE.toOptional() -> Delivery.FAST3
                        else -> Delivery.OFF
                    }
                },
                saleCostLCD = fi.dollersDelivered.map {
                    q -> Formatters.formatSaleCost(q)
                },
                saleQuantityLCD = fi.litersDelivered.map {
                    q -> Formatters.formatSaleQuantity(q)
                },
                priceLCD1 = ShowDollersPump.priceLCD(np.fillActive, fi.price, Fuel.ONE, inputs),
                priceLCD2 = ShowDollersPump.priceLCD(np.fillActive, fi.price, Fuel.TWO, inputs),
                priceLCD3 = ShowDollersPump.priceLCD(np.fillActive, fi.price, Fuel.THREE, inputs),
                sBeep = np.sBeep,
                sSaleComplete = np.sSaleComplete
        )
    }
}

class KeypadPump : Pump {
    override fun create(inputs: Inputs): Outputs {
        val ke = Keypad(inputs.sKeypad, BehaviorSubject.create())
        return Outputs(
                presetLCD = ke.value.map { v ->
                    Formatters.formatSaleCost(v.toDouble())
                },
                sBeep = ke.sBeep
        )
    }
}