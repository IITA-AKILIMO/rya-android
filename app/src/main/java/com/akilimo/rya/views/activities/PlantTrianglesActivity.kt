package com.akilimo.rya.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.adapter.SectionsPagerAdapter
import com.akilimo.rya.databinding.ActivityPlantTrianglesBinding
import com.akilimo.rya.views.fragments.ui.TriangleFragment
import com.akilimo.rya.views.fragments.ui.TriangleThreeFragment
import com.akilimo.rya.views.fragments.ui.TriangleTwoFragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator


class PlantTrianglesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantTrianglesBinding
    private val fragmentArray: MutableList<Fragment> = arrayListOf()
    private var database: AppDatabase? = null
    private var tabPosition = 0;
    private var plantcount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlantTrianglesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        val yieldClass = database?.yieldPrecisionDao()?.findOne()

        if (yieldClass != null) {
            plantcount = yieldClass.plantCount
            fragmentArray.add(TriangleFragment.newInstance(plantcount / 3, "one"))
            fragmentArray.add(TriangleTwoFragment.newInstance(plantcount / 3, "two"))
            fragmentArray.add(TriangleThreeFragment.newInstance(plantcount / 3, "three"))
        }

        val sectionsPagerAdapter =
            SectionsPagerAdapter(supportFragmentManager, lifecycle, fragmentArray)
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Triangle ${position + 1}"
        }.attach()

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabPosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.btnValidate.setOnClickListener {
            var inputValid = false
            when (val activeFragment: Fragment = fragmentArray[tabPosition]) {
                is TriangleFragment -> {
                    inputValid = activeFragment.validateInput()
                }
                is TriangleTwoFragment -> {
                    inputValid = activeFragment.validateInput()
                }
                is TriangleThreeFragment -> {
                    inputValid = activeFragment.validateInput()
                }
            }

            //check if all data has been provided
            if (inputValid) {
                val allData = database?.plantTriangleDao()?.getAll(plantcount)
                if (allData?.size!! == plantcount) {
                    //proceed to next activity
                    val intent =
                        Intent(this@PlantTrianglesActivity, AssessmentActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSwipeLeft(this@PlantTrianglesActivity)
                }
            } else {
                Snackbar.make(
                    it,
                    "PLease provide all root weights in all triangles",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }


    }
}
