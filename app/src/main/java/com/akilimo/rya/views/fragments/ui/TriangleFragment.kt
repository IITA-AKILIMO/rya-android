package com.akilimo.rya.views.fragments.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentTriangleBinding
import com.akilimo.rya.views.fragments.BaseFragment


private const val TRIANGLE_COUNT = "triangle_count"

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleFragment : BaseFragment() {
    private var triangleCount: Int = 0
    private var _binding: FragmentTriangleBinding? = null
    private var ctx: Context? = null

    private var database: AppDatabase? = null
    private val binding get() = _binding!!

    companion object {
        /**
         * @param triangleCount Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(triangleCount: Int) =
            TriangleFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIANGLE_COUNT, triangleCount)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            triangleCount = it.getInt(TRIANGLE_COUNT)
        }
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //let us build the inputs
        val lyt = binding.lytTextField
//        lyt.removeAllViews() //clear all components
        val editTexts = arrayOfNulls<AppCompatEditText>(triangleCount)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
