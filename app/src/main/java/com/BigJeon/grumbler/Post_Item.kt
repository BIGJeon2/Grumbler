package com.BigJeon.grumbler

data class Post_Item (var My_Profile: User, var Effect: Int, var Grade: String, var Date: String, var Content: Custom_TextView){
    constructor() : this(User(), R.drawable.edge_round_white, "", "", Custom_TextView())
}
