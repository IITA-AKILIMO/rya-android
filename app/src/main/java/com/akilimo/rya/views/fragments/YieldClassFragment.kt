package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.adapter.FieldYieldAdapter
import com.akilimo.rya.data.FieldYield
import com.akilimo.rya.databinding.FragmentYieldClassBinding
import com.akilimo.rya.entities.FieldYieldEntity
import com.akilimo.rya.utils.TheItemAnimation
import com.akilimo.rya.utils.Tools
import com.akilimo.rya.widgets.SpacingItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [YieldClassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YieldClassFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentYieldClassBinding? = null

    private var selectedYield: Double = 0.0
    private var database: AppDatabase? = null

    private var fieldYieldEntity: FieldYieldEntity? = null
    private var selectedYieldIndex = -1

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
        fun newInstance() = YieldClassFragment()
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYieldClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldYieldEntity = database?.fieldYieldDao()?.findOne()

        if (fieldYieldEntity != null) {
            selectedYield = fieldYieldEntity?.yieldAmount!!
            selectedYieldIndex = fieldYieldEntity?.rowIndex!!
        }

        val mAdapter = FieldYieldAdapter(
            _context = view.context,
            items = setYieldData(),
            animationType = TheItemAnimation.FADE_IN,
            selectedIndex = selectedYieldIndex
        )

        val recyclerView = binding.rootYieldRecycler

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        recyclerView.addItemDecoration(
            SpacingItemDecoration(2, Tools.dpToPx(view.context, 3), true)
        )

        mAdapter.setOnItemClickListener(object : FieldYieldAdapter.OnItemClickListener {
            override fun onItemClick(view: View, fieldYield: FieldYield, position: Int) {
                mAdapter.setActiveRowIndex(position, view)
                selectedYield = fieldYield.yieldAmount
                fieldYieldEntity = FieldYieldEntity(
                    id = 1,
                    imageId = fieldYield.imageId,
                    yieldLabel = fieldYield.yieldLabel,
                    fieldYieldAmountLabel = fieldYield.fieldYieldAmountLabel,
                    fieldYieldDesc = fieldYield.fieldYieldDesc,
                    yieldAmount = fieldYield.yieldAmount,
                    rowIndex = position
                )
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError? {
        if (selectedYield <= 0.0) {
            val snackBar = Snackbar.make(
                binding.rootYieldRecycler,
                "Please specify field yield to continue",
                Snackbar.LENGTH_SHORT
            )

            snackBar.setAction("OK") {
                snackBar.dismiss()
            }
            snackBar.show()
            return VerificationError("Please specify field yield")
        }

        database?.fieldYieldDao()?.insert(fieldYieldEntity!!)

        return verificationError
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}

    private fun setYieldData(areaUnit: String = "ha"): List<FieldYield> {
        val rd3Tonnes: String
        val rd6Tonnes: String
        val rd9Tonnes: String
        val rd12Tonnes: String
        val rdMore: String
        when (areaUnit.lowercase()) {
            "acre" -> {
                rd3Tonnes = getString(R.string.yield_less_than_3_tonnes_per_acre)
                rd6Tonnes = getString(R.string.yield_3_to_6_tonnes_per_acre)
                rd9Tonnes = getString(R.string.yield_6_to_9_tonnes_per_acre)
                rd12Tonnes = getString(R.string.yield_9_to_12_tonnes_per_acre)
                rdMore = getString(R.string.yield_more_than_12_tonnes_per_acre)
            }
            "ha" -> {
                rd3Tonnes = getString(R.string.yield_less_than_3_tonnes_per_hectare)
                rd6Tonnes = getString(R.string.yield_3_to_6_tonnes_per_hectare)
                rd9Tonnes = getString(R.string.yield_6_to_9_tonnes_per_hectare)
                rd12Tonnes = getString(R.string.yield_9_to_12_tonnes_per_hectare)
                rdMore = getString(R.string.yield_more_than_12_tonnes_per_hectare)
            }
            else -> {
                rd3Tonnes = getString(R.string.yield_less_than_3_tonnes_per_acre)
                rd6Tonnes = getString(R.string.yield_3_to_6_tonnes_per_acre)
                rd9Tonnes = getString(R.string.yield_6_to_9_tonnes_per_acre)
                rd12Tonnes = getString(R.string.yield_9_to_12_tonnes_per_acre)
                rdMore = getString(R.string.yield_more_than_12_tonnes_per_acre)
            }
        }
        val items: MutableList<FieldYield> = ArrayList()
        items.add(
            FieldYield(
                imageId = yieldImages[0],
                imageLabel = getString(R.string.fcy_low),
                yieldLabel = rd3Tonnes,
                fieldYieldAmountLabel = getString(R.string.fcy_lower),
                yieldAmount = 3.75,
                fieldYieldDesc = getString(R.string.lbl_low_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[1],
                imageLabel = getString(R.string.fcy_normal),
                fieldYieldAmountLabel = getString(R.string.fcy_about_the_same),
                yieldLabel = rd6Tonnes,
                yieldAmount = 11.25,
                fieldYieldDesc = getString(R.string.lbl_normal_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[2],
                imageLabel = getString(R.string.fcy_medium),
                fieldYieldAmountLabel = getString(R.string.fcy_somewhat_higher),
                yieldLabel = rd9Tonnes,
                yieldAmount = 18.75,
                fieldYieldDesc = getString(R.string.lbl_high_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[3],
                imageLabel = getString(R.string.fcy_high),
                fieldYieldAmountLabel = getString(R.string.fcy_2_3_times_higher),
                yieldLabel = rd12Tonnes,
                yieldAmount = 26.25,
                fieldYieldDesc = getString(R.string.lbl_very_high_yield)
            )
        )
        items.add(
            FieldYield(
                imageId = yieldImages[4],
                imageLabel = getString(R.string.fcy_very_high),
                fieldYieldAmountLabel = getString(R.string.fcy_more_than_3_times_higher),
                yieldLabel = rdMore,
                yieldAmount = 33.75,
                fieldYieldDesc = getString(R.string.lbl_very_high_yield)
            )
        )
        return items
    }
}
