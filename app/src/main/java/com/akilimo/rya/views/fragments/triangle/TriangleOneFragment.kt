package com.akilimo.rya.views.fragments.triangle

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentTriangleOneBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleOneFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentTriangleOneBinding? = null

    private var database: AppDatabase? = null

    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TriangleOneFragment.
         */
        @JvmStatic
        fun newInstance() = TriangleOneFragment()
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
        _binding = FragmentTriangleOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
