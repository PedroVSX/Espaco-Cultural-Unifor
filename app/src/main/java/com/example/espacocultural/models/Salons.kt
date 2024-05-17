package com.example.espacocultural.models

import android.graphics.drawable.Drawable
import android.widget.ImageView

data class Salons(
    var id: Int,
    var name: String,
    var image: Drawable,
    var showOptions: Boolean
)
