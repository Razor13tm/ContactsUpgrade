package com.example.contacts.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Int,
    var name: String,
    var name2: String,
    var num: String,
    val pic: String
) : Parcelable