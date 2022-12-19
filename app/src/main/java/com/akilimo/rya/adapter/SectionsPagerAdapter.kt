package com.akilimo.rya.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val fragmentArray: MutableList<Fragment>
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentArray.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentArray[position]
    }
}
