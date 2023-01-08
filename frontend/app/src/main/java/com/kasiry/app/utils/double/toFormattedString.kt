package com.kasiry.app.utils.double

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.toFormattedString(): String {
    val formatter = DecimalFormat("#,###.###", DecimalFormatSymbols().apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    })
    return formatter.format(this)
}