package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

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