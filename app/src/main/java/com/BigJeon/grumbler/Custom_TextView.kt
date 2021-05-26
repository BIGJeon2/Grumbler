package com.BigJeon.grumbler

data class Custom_TextView (val x: Float?, val y: Float?, val Color: Int?, val Size: Float, val Text_Bacground_Color: Int?, val Content: String?) {
    constructor(): this(null, null, null, 50f, null, null)
}