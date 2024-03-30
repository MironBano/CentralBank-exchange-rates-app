package com.bano.cbrates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyAdapter(private val currencies: List<Currency>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }

    override fun getItemCount() = currencies.size

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById<TextView>(R.id.currencyNameTextView)
        private val rateTextView: TextView = itemView.findViewById<TextView>(R.id.currencyRateTextView)

        fun bind(currency: Currency) {
            nameTextView.text = currency.name
            rateTextView.text = currency.rate.toString()
        }
    }
}