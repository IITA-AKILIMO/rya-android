package com.akilimo.rya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.akilimo.rya.R
import com.akilimo.rya.entities.CurrencyEntity

class MySpinnerAdapter
constructor(
    val context: Context, private val currencyItems: MutableList<CurrencyEntity>
) : BaseAdapter() {

//    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return currencyItems.size
    }

    override fun getItem(position: Int): Any? {
        return currencyItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.custom_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = currencyItems[position].countryName

        return view
    }

//    @SuppressLint("ViewHolder")
//    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
//        view = layoutInflater.inflate(R.layout.custom_spinner, parent, false)
//        val names = view.findViewById<View>(R.id.spinnerText) as TextView
//        try {
//            val entity = currencyItems[position]
//            names.text = entity.currencyName
//        } catch (ex: Exception) {
//        }
//        return view
//    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.spinnerText) as TextView
        }
    }
}
