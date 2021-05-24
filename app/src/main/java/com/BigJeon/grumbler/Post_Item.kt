package com.BigJeon.grumbler

import java.util.*

data class Post_Item (var My_Profile: User, var Content: String, var Effect: Int?, var Text_Color: String?, var Grade: String, var Posting_Date: String, var Text_1: Custom_TextView?, var Text_2: Custom_TextView?, var Text_3: Custom_TextView?){
    constructor() : this(User(), "", null, null, "", "", null, null, null)
}
