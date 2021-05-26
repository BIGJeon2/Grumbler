package com.BigJeon.grumbler

data class Post_Item (var My_Profile: User, var Effect: Int, var Grade: String, var Posting_Date: String, var Text_1: Custom_TextView?, var Text_2: Custom_TextView?, var Text_3: Custom_TextView?){
    constructor() : this(User(), R.drawable.edge_round_white, "", "", null, null, null)
}
