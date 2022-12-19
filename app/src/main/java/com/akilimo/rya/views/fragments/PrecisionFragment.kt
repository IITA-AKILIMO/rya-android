package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.adapter.YieldPrecisionAdapter
import com.akilimo.rya.data.YieldPrecision
import com.akilimo.rya.databinding.FragmentPrecisionBinding
import com.akilimo.rya.entities.YieldPrecisionEntity
import com.akilimo.rya.utils.TheItemAnimation
import com.google.android.material.snackbar.Snackbar
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [PrecisionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrecisionFragment : BaseStepFragment() {

    private var _binding: FragmentPrecisionBinding? = null
    private var ctx: Context? = null
    private var selectedPrecision: String? = null
    private var triangleCount: Int = -1
    private var plantCount: Int = -1
    private var selectedPrecisionIndex: Int = -1
    private var selectedPrecisionImage: Int = R.drawable.ic_akilimo_logo

    private var database: AppDatabase? = null
    private var fieldInfo: YieldPrecisionEntity? = null

    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = PrecisionFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrecisionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldInfo = database?.yieldPrecisionDao()?.findOne()

        if (fieldInfo != null) {
            selectedPrecision = fieldInfo!!.yieldPrecision
            triangleCount = fieldInfo!!.triangleCount
            plantCount = fieldInfo!!.plantCount
            selectedPrecisionIndex = fieldInfo!!.selectedPrecisionIndex
            selectedPrecisionImage = fieldInfo!!.precisionImage

            binding.precisionImage.setImageResource(selectedPrecisionImage)
        }
        val mAdapter = YieldPrecisionAdapter(
            _context = view.context,
            items = setPrecisionData(),
            animationType = TheItemAnimation.FADE_IN,
            selectedIndex = selectedPrecisionIndex
        )
        val recyclerView = binding.precisionRecycler
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
        }

        mAdapter.setOnItemClickListener(object : YieldPrecisionAdapter.OnItemClickListener {
            override fun onItemClick(view: View, yieldPrecision: YieldPrecision, position: Int) {
                mAdapter.setActiveRowIndex(position, view)
                selectedPrecision = yieldPrecision.precisionType
                triangleCount = yieldPrecision.triangleCount
                plantCount = yieldPrecision.plantCount
                selectedPrecisionIndex = position
                selectedPrecisionImage = yieldPrecision.imageId

                binding.precisionImage.setImageResource(selectedPrecisionImage)
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPrecisionData(): MutableList<YieldPrecision> {
        val items: MutableList<YieldPrecision> = ArrayList()
        items.add(
            YieldPrecision(
                precisionString = getString(R.string.lbl_low_precision),
                precisionType = "low",
                plantCount = 9,
                imageId = R.drawable.triangle_low_precision
            )
        )
        items.add(
            YieldPrecision(
                precisionString = getString(R.string.lbl_medium_precision),
                precisionType = "medium",
                plantCount = 18,
                imageId = R.drawable.triangle_moderate_precision
            )
        )
        items.add(
            YieldPrecision(
                precisionString = getString(R.string.lbl_high_precision),
                precisionType = "high",
                plantCount = 30,
                imageId = R.drawable.triangle_high_precision
            )
        )
        return items
    }

    override fun verifyStep(): VerificationError? {
        if (selectedPrecision.isNullOrEmpty()) {
            val snackBar = Snackbar.make(
                binding.precisionRecycler,
                "Please select a yield class to continue",
                Snackbar.LENGTH_SHORT
            )

            snackBar.setAction("OK") {
                snackBar.dismiss()
            }
            snackBar.show()
            return VerificationError("Please select a yield class")
        }
        if (fieldInfo == null) {
            fieldInfo = YieldPrecisionEntity(id = 1)
        }
        fieldInfo?.yieldPrecision = selectedPrecision!!
        fieldInfo?.triangleCount = triangleCount
        fieldInfo?.plantCount = plantCount
        fieldInfo?.selectedPrecisionIndex = selectedPrecisionIndex
        fieldInfo?.precisionImage = selectedPrecisionImage


        database!!.yieldPrecisionDao().insert(fieldInfo!!)
        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
        Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()
    }
}
