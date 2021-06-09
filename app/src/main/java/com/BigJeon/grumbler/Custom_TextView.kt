package com.BigJeon.grumbler

data class Custom_TextView (val Color: Int, val Size: Float, val Bacground_Color: Int, val Content: String, val Font: Int?) {
    constructor(): this( R.color.black, 50f, android.R.color.transparent, "", null)
}