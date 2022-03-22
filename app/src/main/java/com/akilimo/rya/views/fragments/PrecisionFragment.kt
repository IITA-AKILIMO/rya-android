package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.adapter.YieldPrecisionAdapter
import com.akilimo.rya.data.YieldPrecision
import com.akilimo.rya.databinding.FragmentPrecisionBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.YieldPrecisionEntity
import com.akilimo.rya.utils.TheItemAnimation
import com.akilimo.rya.utils.Tools
import com.akilimo.rya.widgets.SpacingItemDecoration
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
    private var triangleCount: Int = 0;
    private var plantCount: Int = 0;

    private var database: AppDatabase? = null
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrecisionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter =
            YieldPrecisionAdapter(view.context, setPrecisionData(), TheItemAnimation.FADE_IN)
        val recyclerView = binding.precisionRecycler
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(view.context)
//            layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
//        recyclerView.addItemDecoration(
//            SpacingItemDecoration(2, Tools.dpToPx(view.context, 3), true)
//        )

        mAdapter.setOnItemClickListener(object : YieldPrecisionAdapter.OnItemClickListener {
            override fun onItemClick(view: View, yieldPrecision: YieldPrecision, position: Int) {
                mAdapter.setActiveRowIndex(position, view)
                selectedPrecision = yieldPrecision.precisionType
                triangleCount = yieldPrecision.triangleCount
                plantCount = yieldPrecision.plantCount
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
                plantCount = 9
            )
        )
        items.add(
            YieldPrecision(
                precisionString = getString(R.string.lbl_medium_precision),
                precisionType = "medium",
                plantCount = 18
            )
        )
        items.add(
            YieldPrecision(
                precisionString = getString(R.string.lbl_high_precision),
                precisionType = "high",
                plantCount = 30
            )
        )
        return items
    }

    override fun verifyStep(): VerificationError? {
        if (selectedPrecision.isNullOrEmpty()) {
            return VerificationError("Please select a yield class")
        }
        var fieldInfo = database!!.yieldPrecisionDao().findOne()
        if (fieldInfo == null) {
            fieldInfo = YieldPrecisionEntity(
                yieldPrecision = selectedPrecision!!,
                triangleCount = triangleCount,
                plantCount = plantCount
            )
            database!!.yieldPrecisionDao().insert(fieldInfo)
        } else {
            fieldInfo.yieldPrecision = selectedPrecision!!
            fieldInfo.triangleCount = triangleCount
            fieldInfo.plantCount = plantCount
            database!!.yieldPrecisionDao().update(fieldInfo)
        }

        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
        Toast.makeText(context, error.errorMessage, Toast.LENGTH_SHORT).show()
    }
}
