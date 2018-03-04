package mobi.pooh3.frpstudy.petrol.domain

import mobi.pooh3.frpstudy.petrol.*
import java.util.*


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
