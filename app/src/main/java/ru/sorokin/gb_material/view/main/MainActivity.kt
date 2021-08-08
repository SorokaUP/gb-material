package ru.sorokin.gb_material.view.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import me.relex.circleindicator.CircleIndicator
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.settings.SettingsData
import ru.sorokin.gb_material.util.addFragmentWithBackStack
import ru.sorokin.gb_material.view.pod.PODFragment
import ru.sorokin.gb_material.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readSettings()
        setCurrentTheme()

        setContentView(R.layout.main_activity)

        initViewPager()
    }

    private fun initViewPager() {
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        adapter.setStringResources { i: Int -> getString(i) }

        val viewPager = findViewById<ViewPager>(R.id.main_view_pager)
        viewPager.adapter = adapter

        val indicator = findViewById<CircleIndicator>(R.id.main_indicator)
        indicator.setViewPager(viewPager)
    }

    private fun readSettings() {
        val sharedPreferences =
            getSharedPreferences(SettingsData.PREFERENCE_NAME, Context.MODE_PRIVATE)

        SettingsData.currentTheme = sharedPreferences
            .getInt(SettingsData.CURRENT_THEME_PREF_NAME, SettingsData.THEME_INDIGO)

        SettingsData.earthLon =
            sharedPreferences.getFloat(SettingsData.CURRENT_EARTH_LON, SettingsData.EARTH_DEF_LON)
        SettingsData.earthLat =
            sharedPreferences.getFloat(SettingsData.CURRENT_EARTH_LAT, SettingsData.EARTH_DEF_LAT)
        SettingsData.earthDim =
            sharedPreferences.getFloat(SettingsData.CURRENT_EARTH_DIM, SettingsData.EARTH_DEF_DIM)
    }

    private fun setCurrentTheme() {
        if (SettingsData.currentTheme == SettingsData.THEME_INDIGO) {
            setTheme(SettingsData.THEME_INDIGO_ID)
        } else {
            setTheme(SettingsData.THEME_PINK_ID)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (doAction(item.itemId)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doAction(id: Int): Boolean {
        when (id) {
            R.id.settings_action -> {
                supportFragmentManager.addFragmentWithBackStack(SettingsFragment())
                return true
            }
        }
        return false
    }
}