package ru.sorokin.gb_material.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.view.*
import ru.sorokin.gb_material.view.earth.EarthFragment
import ru.sorokin.gb_material.view.mars.MarsFragment
import ru.sorokin.gb_material.view.pod.PODFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    private var stringResources: ((Int) -> String)? = null

    private val fragments = arrayOf(
        PODFragment(),
        EarthFragment(),
        MarsFragment()
    )
    private var titles = arrayOfNulls<String>(fragments.size)

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    fun setStringResources(stringResources: (Int) -> String) {
        this.stringResources = stringResources
        titles = arrayOf(
            getString(R.string.pod_description),
            getString(R.string.earth_description),
            getString(R.string.mars_description)
        )
    }

    private fun getString(id: Int) = stringResources?.invoke(id)
}