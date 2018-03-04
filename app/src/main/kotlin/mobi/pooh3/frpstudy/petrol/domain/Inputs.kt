package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

data class Inputs(
        val sNozzle1: Observable<UpDown>,
        val sNozzle2: Observable<UpDown>,
        val sNozzle3: Observable<UpDown>,
        val sKeypad: Observable<Key>,
        val sFuelPluses: Observable<Int>,
        val price1: BehaviorSubject<Double>,
        val price2: BehaviorSubject<Double>,
        val price3: BehaviorSubject<Double>,
        val sClearSale: Observable<Unit>
)