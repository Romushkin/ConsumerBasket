package com.example.consumerbasket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter(private val context: Context, private val productList: List<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, productList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val idTV = view?.findViewById<TextView>(R.id.idTV)
        val nameTV = view?.findViewById<TextView>(R.id.nameTV)
        val weightTV = view?.findViewById<TextView>(R.id.weightTV)
        val priceTV = view?.findViewById<TextView>(R.id.priceTV)

        idTV?.text = item?.id.toString()
        nameTV?.text = item?.name
        weightTV?.text = item?.weight.toString()
        priceTV?.text = item?.price.toString()

        return view!!
    }
}