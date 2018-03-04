package mobi.pooh3.frpstudy.petrol.domain


import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols

import java.util.Locale

object Formatters {
    var priceFmt = DecimalFormat("#0.0000000",
            DecimalFormatSymbols(Locale.US))

    var costFmt = DecimalFormat("#0.00",
            DecimalFormatSymbols(Locale.US))

    var quantityFmt = DecimalFormat("#0.00",
            DecimalFormatSymbols(Locale.US))

    fun formatPrice(price: Double): String {
        val lcdSize = 4
        val text = priceFmt.format(price)
        var i = 0
        var digits = 0
        while (true) {
            while (i < text.length && text[i] == '.') i++
            if (digits == lcdSize) return text.substring(0, i)
            if (i >= text.length) break
            digits++
            i++
        }
        return text
    }

    fun formatSaleCost(cost: Double): String {
        return costFmt.format(cost)
    }

    fun formatSaleQuantity(quantity: Double): String {
        return quantityFmt.format(quantity)
    }
}