package com.akilimo.rya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.akilimo.rya.R
import com.akilimo.rya.data.FieldYield
import com.akilimo.rya.databinding.YieldCardLayoutBinding
import com.akilimo.rya.utils.TheItemAnimation

class FieldYieldAdapter(
    private val _context: Context,
    private val items: List<FieldYield>,
    private val animationType: Int,
    private val selectedIndex: Int = -1
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var rowIndex = -1
    private var lastPosition = -1
    private val onAttach = true
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        rowIndex = selectedIndex
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, fieldYield: FieldYield, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            YieldCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OriginalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val fieldYield = items[position]
            val currentYieldAmount = fieldYield.yieldAmount
            val imageLabel = fieldYield.imageLabel
            val yieldLabel = fieldYield.yieldLabel
            val yieldImage = fieldYield.imageId

            holder.yieldName.text = yieldLabel
            holder.imageLabel.text = imageLabel
            holder.rootYieldImage.setImageResource(yieldImage)

            holder.mainCard.setOnClickListener { theView ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(theView, items[position], position)
                }
            }
            var color = ContextCompat.getColor(_context, R.color.grey_5)
            if (rowIndex == position) {
                color = ContextCompat.getColor(_context, R.color.akilimoLightGreen)
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

    inner class OriginalViewHolder(binding: YieldCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageLabel: TextView = binding.imageLabel
        val yieldName: TextView = binding.yieldName
        val rootYieldImage: ImageView = binding.yieldImage
        val mainCard: CardView = binding.yieldCard
    }
}
