package com.BigJeon.grumbler

import android.net.Uri

data class User(var My_UID: String? = null,
                var My_Name: String? = null,
                var My_Img: String? = null,
                var My_Favorites: ArrayList<String>? = null,
                var My_Friends: ArrayList<String>? = null)
