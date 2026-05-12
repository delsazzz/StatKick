package com.example.app_futbol_tfg.ui.utils

import android.content.Context
import com.example.app_futbol_tfg.R

fun getDrawableId(context: Context, name: String?): Int {
    return try {
        if (name.isNullOrEmpty()) return R.drawable.football_ball

        val resId = context.resources.getIdentifier(
            name.lowercase().replace(" ", "_"),
            "drawable",
            context.packageName
        )
        if (resId != 0) resId else R.drawable.football_ball
    } catch (e: Exception) {
        e.printStackTrace()
        R.drawable.football_ball
    }
}