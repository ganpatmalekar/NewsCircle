package com.gsm.newscircle.ui.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.Country
import com.gsm.newscircle.databinding.ItemCountryBinding
import com.gsm.newscircle.utils.ItemClickListener

class CountryListAdapter :
    ListAdapter<Country, CountryListAdapter.CountryViewHolder>(CountryDiffCallback()) {

    var selectedCountryCode: String = ""
    lateinit var itemClickListener: ItemClickListener<Country>

    fun updateCountryData(list: List<Country>, code: String) {
        selectedCountryCode = code
        submitList(list)
    }

    class CountryDiffCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(
            old: Country,
            new: Country
        ): Boolean = old.code == new.code

        override fun areContentsTheSame(
            old: Country,
            new: Country
        ): Boolean = old == new
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): CountryViewHolder {
        return CountryViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    inner class CountryViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country, itemClickListener: ItemClickListener<Country>) {
            binding.apply {
                tvCountryName.text = country.name
                tvCountryCode.text = country.flag

                if (country.code == selectedCountryCode) {
                    root.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.lightSkyBlue
                        )
                    )
                } else {
                    root.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            android.R.color.transparent
                        )
                    )
                }

                root.setOnClickListener {
                    itemClickListener.invoke(country)
                }
            }
        }
    }
}