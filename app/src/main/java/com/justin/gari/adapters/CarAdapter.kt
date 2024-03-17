package com.justin.gari.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.justin.gari.R
import com.justin.gari.activities.DetailActivity
import com.justin.gari.databinding.ListItemBinding
import com.justin.gari.models.carModels.Car
import com.squareup.picasso.Picasso
import java.util.Locale

class CarAdapter(private var carList: List<Car>, val context: Context) : BaseAdapter(), Filterable {
    private val initialCarDataList = ArrayList(carList)

    override fun getCount(): Int = carList.size

    override fun getItem(position: Int): Any = carList[position]

    override fun getItemId(position: Int): Long = carList[position].car_id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ListItemBinding = if (convertView == null) {
            ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ListItemBinding.bind(convertView)
        }

        val car = carList[position]

        with(binding) {
            tvCarName.text = car.car_name
            tvDriveOption.text = car.drive?.joinToString(", ") ?: "N/A"
            tvTransmission.text = car.transmission
            tvPrice.text = context.getString(R.string.ksh_price, car.price)
            Picasso.get()
                .load(if (car.car_images.isNotEmpty()) car.car_images[0] else "")
                .fit()
                .placeholder(R.drawable.vehicle_placeholder)
                .error(R.drawable.vehicle_placeholder)
                .into(tvImage)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("car_id", car.car_id.toString())
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context.startActivity(intent)
            }
        }

        return binding.root
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<Car> = if (constraint.isNullOrEmpty()) {
                    initialCarDataList
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT)
                    initialCarDataList.filter {
                        it.car_name.lowercase(Locale.ROOT).contains(filterPattern)
                                || it.transmission.lowercase(Locale.ROOT).contains(filterPattern)
                                || it.drive?.any { drive -> drive.lowercase(Locale.ROOT).contains(filterPattern) } == true
                    } as ArrayList<Car>
                }
                return FilterResults().apply { values = filteredList }
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                carList = results?.values as List<Car>
                notifyDataSetChanged()
            }
        }
    }
}
