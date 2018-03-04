package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import android.media.ToneGenerator
import android.media.AudioManager
import io.reactivex.rxkotlin.withLatestFrom
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.rx.unOptional
import java.util.*


enum class End { END }
enum class Fuel { ONE, TWO, THREE }
enum class Delivery { FAST1, FAST2, FAST3, OFF }
class Sale
class Key
enum class UpDown { UP, DOWN }


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

fun beep() {
    val toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME/2)
    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP)
}









class PetrolDumpFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_petrol_dump, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)
    }


    companion object {
        fun newInstance(): PetrolDumpFragment {
            return PetrolDumpFragment()
        }
    }

}


