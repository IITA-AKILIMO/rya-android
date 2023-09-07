package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentPlotResultsBinding
import com.akilimo.rya.views.fragments.BaseStepFragment


/**
 * A simple [Fragment] subclass.
 * Use the [PlotResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlotResultsFragment(private val ryaEndpoint: String) : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentPlotResultsBinding? = null
    private var database: AppDatabase? = null


    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(ryaEndpoint: String) = PlotResultsFragment(ryaEndpoint)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetry.setOnClickListener { refreshPlotData() }
    }

    override fun onSelected() {
        super.onSelected()
        refreshPlotData()
    }

    private fun refreshPlotData() {

        if (!binding.shimmerViewContainer.isShimmerStarted) {
            binding.shimmerViewContainer.startShimmer()
        }
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.widgetGroup.visibility = View.GONE


        val estimateResults = database?.estimateResultsDao()?.findOne()

        if (estimateResults != null) {
            val totalEstimate = estimateResults.tonnageEstimate * estimateResults.tonnagePrice
            binding.tonnageResults.text =
                "Estimated value: $totalEstimate ${estimateResults.currency}"
            binding.tonnageResultsSub.text =
                "(With a price of ${estimateResults.tonnagePrice} ${estimateResults.currency} per tonne)"
        }
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlotResultsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
