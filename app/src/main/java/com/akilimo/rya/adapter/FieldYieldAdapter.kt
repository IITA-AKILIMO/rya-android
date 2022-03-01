package com.akilimo.rya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.akilimo.rya.data.FieldYield
import java.text.FieldPosition
import com.akilimo.rya.R;

class FieldYieldAdapter(
    private val _context: Context,
    private val items: List<FieldYield>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, fieldYield: FieldYield, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_recommendation_image, parent, false)
        viewHolder = OriginalViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val fieldYield = items[position]
            val currentYieldAmount = fieldYield.yieldAmount
            val yieldLabel = fieldYield.yieldLabel
            val yieldImage = fieldYield.imageId

            holder.name.text = yieldLabel
            holder.rootYieldImage.setImageResource(yieldImage)

            holder.mainCard.setOnClickListener { theView ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(theView, items[position], position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rootYieldImage: ImageView = view.findViewById(R.id.yieldImage)
        var name: TextView = view.findViewById(R.id.yieldName)
        var mainCard: CardView = view.findViewById(R.id.yieldCard)
    }
}
