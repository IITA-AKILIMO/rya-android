package com.akilimo.rya.views.activities

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.adapter.SectionsPagerAdapter
import com.akilimo.rya.databinding.ActivityPlantTrianglesBinding
import com.akilimo.rya.views.fragments.PlaceholderFragment
import com.akilimo.rya.views.fragments.ui.TriangleFragment
import com.google.android.material.tabs.TabLayoutMediator


class PlantTrianglesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantTrianglesBinding
    private val fragmentArray: MutableList<Fragment> = arrayListOf()
    private var database: AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantTrianglesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        val yieldClass = database?.yieldPrecisionDao()?.findOne()

        if (yieldClass != null) {
            fragmentArray.add(TriangleFragment.newInstance(yieldClass.triangleCount / 3))
            fragmentArray.add(TriangleFragment.newInstance(yieldClass.triangleCount / 3))
            fragmentArray.add(TriangleFragment.newInstance(yieldClass.triangleCount / 3))
        }

        val sectionsPagerAdapter =
            SectionsPagerAdapter(supportFragmentManager, lifecycle, fragmentArray)
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Triangle ${position + 1}"
        }.attach()


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}
