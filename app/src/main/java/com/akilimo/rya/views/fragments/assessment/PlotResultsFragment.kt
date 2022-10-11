package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentAssessmentResultsBinding
import com.akilimo.rya.databinding.FragmentPlotResultsBinding
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.request.RyaPlot
import com.akilimo.rya.views.fragments.BaseStepFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [PlotResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlotResultsFragment(private val ryaEndpoint: String) : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentPlotResultsBinding? = null
    private var database: AppDatabase? = null
    private var apiInterface: ApiInterface? = null


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
        apiInterface = ApiInterface.create(ryaEndpoint)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshPlotData()
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

            renderPlot(RyaPlot(fileName = estimateResults.fileNameLean))
        }
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlotResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun renderPlot(ryaPlot: RyaPlot) {
        val imageView = binding.yieldPlotImage


        val plotReader = apiInterface?.readPlot(ryaPlot)

        plotReader?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    val bytes = response.body()!!.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                  imageView.setImage(ImageSource.bitmap(bitmap))
                    imageView.setImageBitmap(bitmap)
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.widgetGroup.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                Toast.makeText(
                    ctx, "Unable to load plot data", Toast.LENGTH_SHORT
                ).show();
            }

        })
    }
}
