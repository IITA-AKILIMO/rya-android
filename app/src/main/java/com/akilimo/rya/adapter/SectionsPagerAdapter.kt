package com.akilimo.rya.adapter

import android.content.Context
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akilimo.rya.R
import com.akilimo.rya.views.fragments.PlaceholderFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return PlaceholderFragment.newInstance()
    }
}
