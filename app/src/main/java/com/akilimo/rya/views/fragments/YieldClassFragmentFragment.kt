package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.adapter.FieldYieldAdapter
import com.akilimo.rya.data.FieldYield
import com.akilimo.rya.databinding.FragmentYieldClassBinding
import com.akilimo.rya.entities.FieldYieldEntity
import com.akilimo.rya.utils.TheItemAnimation
import com.akilimo.rya.utils.Tools
import com.akilimo.rya.widgets.SpacingItemDecoration
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [YieldClassFragmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YieldClassFragmentFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentYieldClassBinding? = null

    private var selectedYield: Double = 0.0
    private var database: AppDatabase? = null

    private val yieldImages = arrayOf(
        R.drawable.yield_less_than_7point5,
        R.drawable.yield_7point5_to_15,
        R.drawable.yield_15_to_22point5,
        R.drawable.yield_22_to_30,
        R.drawable.yield_more_than_30
    )

    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = YieldClassFragmentFragment()
    }


    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYieldClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = FieldYieldAdapter(view.context, setYieldData(), TheItemAnimation.FADE_IN)

        val recyclerView = binding.rootYieldRecycler
//        val mLayoutManager = LinearLayoutManager(view.context)
//        recyclerView.layoutManager = mLayoutManager
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = mAdapter

        recyclerView.apply {
            adapter = mAdapter
//            layoutManager = LinearLayoutManager(view.context)
            layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        recyclerView.addItemDecoration(
            SpacingItemDecoration(2, Tools.dpToPx(view.context, 3), true)
        )

        mAdapter.setOnItemClickListener(object : FieldYieldAdapter.OnItemClickListener {
            override fun onItemClick(view: View, fieldYield: FieldYield, position: Int) {
                mAdapter.setActiveRowIndex(position, view)
                selectedYield = fieldYield.yieldAmount;
                val fieldYieldEntity = FieldYieldEntity(
                    id = 1,
                    imageId = fieldYield.imageId,
                    yieldLabel = fieldYield.yieldLabel,
                    fieldYieldAmountLabel = fieldYield.fieldYieldAmountLabel,
                    fieldYieldDesc = fieldYield.fieldYieldDesc,
                    yieldAmount = fieldYield.yieldAmount
                )
                database?.fieldYieldDao()?.insert(fieldYieldEntity)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError? {
        if (selectedYield <= 0.0) {
            return VerificationError("Please specify field yield")
        }

        return verificationError
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}

    private fun setYieldData(areaUnit: String = "ha"): List<FieldYield> {
        var rd_3_tonnes: String
        var rd_6_tonnes: String
        var rd_9_tonnes: String
        var rd_12_tonnes: String
        var rd_more: String
        when (areaUnit.lowercase()) {
            "acre" -> {
                rd_3_tonnes = getString(R.string.yield_less_than_3_tonnes_per_acre)
                rd_6_tonnes = getString(R.string.yield_3_to_6_tonnes_per_acre)
                rd_9_tonnes = getString(R.string.yield_6_to_9_tonnes_per_acre)
                rd_12_tonnes = getString(R.string.yield_9_to_12_tonnes_per_acre)
                rd_more = getString(R.string.yield_more_than_12_tonnes_per_acre)
            }
            "ha" -> {
                rd_3_tonnes = getString(R.string.yield_less_than_3_tonnes_per_hectare)
                rd_6_tonnes = getString(R.string.yield_3_to_6_tonnes_per_hectare)
                rd_9_tonnes = getString(R.string.yield_6_to_9_tonnes_per_hectare)
                rd_12_tonnes = getString(R.string.yield_9_to_12_tonnes_per_hectare)
                rd_more = getString(R.string.yield_more_than_12_tonnes_per_hectare)
            }
            else -> {
                rd_3_tonnes = getString(R.string.yield_less_than_3_tonnes_per_acre)
                rd_6_tonnes = getString(R.string.yield_3_to_6_tonnes_per_acre)
                rd_9_tonnes = getString(R.string.yield_6_to_9_tonnes_per_acre)
                rd_12_tonnes = getString(R.string.yield_9_to_12_tonnes_per_acre)
                rd_more = getString(R.string.yield_more_than_12_tonnes_per_acre)
            }
        }
        val items: MutableList<FieldYield> = ArrayList()
        items.add(
            FieldYield(
                imageId = yieldImages[0],
                yieldLabel = getString(R.string.fcy_lower),
                fieldYieldAmountLabel = rd_3_tonnes,
                yieldAmount = 3.75,
                fieldYieldDesc = getString(R.string.lbl_low_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[1],
                fieldYieldAmountLabel = getString(R.string.fcy_about_the_same),
                yieldLabel = rd_6_tonnes,
                yieldAmount = 11.25,
                fieldYieldDesc = getString(R.string.lbl_normal_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[2],
                fieldYieldAmountLabel = getString(R.string.fcy_somewhat_higher),
                yieldLabel = rd_9_tonnes,
                yieldAmount = 18.75,
                fieldYieldDesc = getString(R.string.lbl_high_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[3],
                fieldYieldAmountLabel = getString(R.string.fcy_2_3_times_higher),
                yieldLabel = rd_12_tonnes,
                yieldAmount = 26.25,
                fieldYieldDesc = getString(R.string.lbl_very_high_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[4],
                fieldYieldAmountLabel = getString(R.string.fcy_more_than_3_times_higher),
                yieldLabel = rd_more,
                yieldAmount = 33.75,
                fieldYieldDesc = getString(R.string.lbl_very_high_yield)
            )
        )
        return items
    }
}
