package com.akilimo.rya.views.activities

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.akilimo.rya.adapter.SectionsPagerAdapter
import com.akilimo.rya.databinding.ActivityPlantTrianglesBinding
import com.google.android.material.tabs.TabLayoutMediator


class PlantTrianglesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantTrianglesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantTrianglesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Triangle $position"
        }.attach()


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}
