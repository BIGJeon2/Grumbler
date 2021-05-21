package com.BigJeon.grumbler

import java.util.*

data class Post_Item (var My_Profile: User, var Content: String, var Effect: Int?, var Text_Color: String?, var Grade: String, var Posting_Date: String){
    constructor() : this(User(), "", null, null, "", "")
}
