package com.justin.gari.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.justin.gari.R
import com.justin.gari.activities.DetailActivity
import com.justin.gari.models.carModels.Cars
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CarAdapter(private val carList: ArrayList<Cars>, val context: Context) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var carNameTextView: TextView
    private lateinit var driveOptionTextView: TextView
    private lateinit var transmissionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var carImageView: ImageView
    private lateinit var itemView : CardView

    val initialCarDataList = ArrayList<Cars>().apply {
        carList.let { addAll(it) }
    }

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return carList.size
    }

    fun getFilter(): Filter {
        return carFilter
    }

    private val carFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Cars> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialCarDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialCarDataList.forEach {
                    if (it.car_name?.toLowerCase(Locale.ROOT)?.contains(query) == true) {
                        filteredList.add(it)
                    } else if(it.transmission?.toLowerCase(Locale.ROOT)?.contains(query) == true) {
                        filteredList.add(it)
                    } else if(it.engine?.toLowerCase(Locale.ROOT)?.contains(query) == true) {
                        filteredList.add(it)
                    }else if(it.drive?.toLowerCase(Locale.ROOT)?.contains(query) == true) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                carList.clear()
                carList.addAll(results.values as ArrayList<Cars>)
                notifyDataSetChanged()
            }
        }
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView

        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.list_item, null)
        }

        carNameTextView = convertView!!.findViewById(R.id.tvCarName)
        driveOptionTextView = convertView.findViewById(R.id.tvDriveOption)
        transmissionTextView = convertView.findViewById(R.id.tvTransmission)
        carImageView = convertView.findViewById(R.id.tvImage)
        priceTextView = convertView.findViewById(R.id.tvPrice)
        itemView= convertView.findViewById(R.id.itemView)

        carNameTextView.text = carList[position].car_name
        driveOptionTextView.text = carList[position].drive
        transmissionTextView.text = carList[position].transmission
        Picasso.get()
            .load(carList[position].front_view)
            .fit()
            .placeholder(R.drawable.vehicle_placeholder)
            .error(R.drawable.vehicle_placeholder)
            .into(carImageView)
        priceTextView.text = "Ksh. " + carList[position].price

        itemView.setOnClickListener {
            //get position of selected item
            val car = carList[position]

            val carId: String? = car.car_id

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("car_id", carId)
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        return convertView
    }
}
//<CarAdapter.ViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return ViewHolder(layout)
//    }
//
//    override fun getItemCount(): Int {
//        return carList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindView(carList[position])
//
//        holder.itemView.setOnClickListener {
//            //get position of selected item
//            val car = carList[position]
//
//            val carId: String? = car.car_id
//
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("car_id", carId)
//            context.startActivity(intent)
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
//        private val driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
//        private val transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
//        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
////        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
//        private val imageView: ImageView =itemView.findViewById(R.id.tvImage)
//
//        fun bindView(cars: Cars) {
//            carNameTextView.text = cars.car_name
//            driveOptionTextView.text = cars.drive
//            transmissionTextView.text = cars.transmission
//            Picasso.get().load(cars.front_view).into(imageView);
//            priceTextView.text = "Ksh. " +cars.price
////            statusTextView.text = cars.status
//        }
//    }
//}