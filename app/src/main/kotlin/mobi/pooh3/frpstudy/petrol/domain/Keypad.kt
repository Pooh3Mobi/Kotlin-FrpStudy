package mobi.pooh3.frpstudy.petrol.domain

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.gate
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.extensions.rx.unOptional
import java.util.*


class Keypad(sKeypad: Observable<Key>, sClear: Observable<Unit>) {
    val value: BehaviorSubject<Int>
    val sBeep: Observable<Unit>

    constructor(sKeypad: Observable<Key>,
                sClear: Observable<Unit>,
                active: BehaviorSubject<Boolean>) :
            this(sKeypad.gate(active), sClear) {
    }

    init {
        val value = BehaviorSubject.create<Int>()
        this.value = value
        val sKeyUpdate =
                sKeypad.withLatestFrom(value,
                        { key, value_ ->
                            if (key === Key.CLEAR)
                                return@withLatestFrom Optional.of(0)
                            else {
                                val x10 = value_ * 10
                                return@withLatestFrom if (x10 >= 1000)
                                    Optional.empty()
                                else
                                    Optional.of(
                                            when {
                                                key === Key.ZERO  -> x10
                                                key === Key.ONE   -> x10 + 1
                                                key === Key.TWO   -> x10 + 2
                                                key === Key.THREE -> x10 + 3
                                                key === Key.FOUR  -> x10 + 4
                                                key === Key.FIVE  -> x10 + 5
                                                key === Key.SIX   -> x10 + 6
                                                key === Key.SEVEN -> x10 + 7
                                                key === Key.EIGHT -> x10 + 8
                                                else              -> x10 + 9
                                            }
                                    )
                            }
                        }
                ).unOptional()

        value.loop(
                Observable.merge(
                        sKeyUpdate,
                        sClear.map { 0 }
                ).hold(0)
        )

        sBeep = sKeyUpdate.flatMap{ Observable.empty<Unit>() }
    }
}
