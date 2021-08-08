package ru.sorokin.gb_material.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.SettingsData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readSettings()
        setCurrentTheme()

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commitNow()
        }
    }

    private fun readSettings() {
        SettingsData.CURRENT_THEME =
            getSharedPreferences(SettingsData.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getInt(SettingsData.CURRENT_THEME_PREF_NAME, SettingsData.THEME_INDIGO)
    }

    private fun setCurrentTheme() {
        if (SettingsData.CURRENT_THEME == SettingsData.THEME_PINK) {
            setTheme(SettingsData.THEME_INDIGO_ID)
        } else {
            setTheme(SettingsData.THEME_PINK_ID)
        }
    }
}