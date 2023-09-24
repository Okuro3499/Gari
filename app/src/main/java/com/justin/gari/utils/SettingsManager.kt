package com.justin.gari.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private var mySettings: SharedPreferences
    init {
        mySettings = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }



    fun setNightModeState(state: Boolean?) {
        val editor: SharedPreferences.Editor = mySettings.edit()
        editor.putBoolean("Night Mode", state!!)
        editor.apply()
    }

    fun loadNightModeState(): Boolean {
        return mySettings.getBoolean("Night Mode", false)
    }
}