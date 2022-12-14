package com.akilimo.rya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akilimo.rya.R
import com.akilimo.rya.data.YieldPrecision
import com.akilimo.rya.utils.TheItemAnimation

class YieldPrecisionAdapter(
    private val _context: Context,
    private val items: MutableList<YieldPrecision>,
    private val animationType: Int,
    private val selectedIndex: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var rowIndex = -1
    private var lastPosition = -1
    private val onAttach = true
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        rowIndex = selectedIndex
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, yieldPrecision: YieldPrecision, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.precision_card_layout, parent, false)
        viewHolder = OriginalViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val precision = items[position]
            holder.name.text = precision.precisionString

            holder.mainCard.setOnClickListener { theView ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(theView, items[position], position)
                }
            }
            var color = ContextCompat.getColor(_context, R.color.grey_5)
            if (rowIndex == position) {
                color = ContextCompat.getColor(_context, R.color.blue_A100)
            }

            holder.mainCard.setCardBackgroundColor(color)
            setAnimation(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setActiveRowIndex(position: Int, view: View) {
        notifyItemChanged(rowIndex)
        rowIndex = position
        notifyItemChanged(position)
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            TheItemAnimation.animate(view, if (onAttach) position else -1, animationType)
            lastPosition = position
        }
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.precisionText)
        var mainCard: CardView = view.findViewById(R.id.precisionCard)
    }
}
