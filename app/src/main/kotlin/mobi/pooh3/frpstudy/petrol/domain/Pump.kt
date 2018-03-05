package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


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

data class Sale(
        val fuel: Fuel,
        val price: Double,
        val cost: Double,
        val quantity: Double
)





