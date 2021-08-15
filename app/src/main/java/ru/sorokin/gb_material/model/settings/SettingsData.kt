package ru.sorokin.gb_material.model.settings

import ru.sorokin.gb_material.R

object SettingsData {
    const val PREFERENCE_NAME = "Settings"
    const val CURRENT_THEME_PREF_NAME = "THEME"
    const val CURRENT_EARTH_LON = "EARTH_LON"
    const val CURRENT_EARTH_LAT = "EARTH_LAT"
    const val CURRENT_EARTH_DIM = "EARTH_DIM"
    const val WIKI_URI = "https://@.wikipedia.org/wiki/" // @ заменяется текущим языком [ru|com]

    //region Theme
    const val THEME_INDIGO_ID = R.style.AppTheme_IndigoTheme
    const val THEME_PINK_ID = R.style.AppTheme_PinkTheme
    const val THEME_INDIGO = 0
    const val THEME_PINK = 1
    //endregion
    //region POD
    const val TODAY_PHOTO = 0
    const val YESTERDAY_PHOTO = 1
    const val DAY_BEFORE_YESTERDAY_PHOTO = 2
    //endregion
    //region Earth
    const val EARTH_DEF_LON = 37.883211F
    const val EARTH_DEF_LAT = 47.908572F
    const val EARTH_DEF_DIM = 0.1F
    //endregion
    //region Mars
    private const val MARS_ROVER_CAMERA_FHAZ = "fhaz"
    //endregion

    var currentTheme = THEME_INDIGO_ID
    var dayOfPhoto = TODAY_PHOTO
    var earthLon = EARTH_DEF_LON
    var earthLat = EARTH_DEF_LAT
    var earthDim = EARTH_DEF_DIM
    var marsRoverCamera = MARS_ROVER_CAMERA_FHAZ
}