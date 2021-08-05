package ru.sorokin.gb_material.model

import ru.sorokin.gb_material.R

object SettingsData {
    const val PREFERENCE_NAME = "Settings"
    const val CURRENT_THEME_PREF_NAME = "THEME"

    const val THEME_INDIGO_ID = R.style.AppTheme_IndigoTheme
    const val THEME_PINK_ID = R.style.AppTheme_PinkTheme
    const val THEME_INDIGO = 0
    const val THEME_PINK = 1

    var CURRENT_THEME = THEME_INDIGO
}