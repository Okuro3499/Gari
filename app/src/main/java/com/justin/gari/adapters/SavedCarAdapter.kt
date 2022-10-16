package com.justin.gari.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.justin.gari.R
import com.justin.gari.models.saveCarModels.SaveCarObject
import com.squareup.picasso.Picasso

internal class SavedCarAdapter(private val savedCarList: List<SaveCarObject>, val context: Context) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var carNameTextView: TextView
    private lateinit var driveOptionTextView: TextView
    private lateinit var transmissionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var carImageView: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return savedCarList.size
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
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null. If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.list_item, null)
        }

        carNameTextView = convertView!!.findViewById(R.id.tvCarName)
        driveOptionTextView = convertView.findViewById(R.id.tvDriveOption)
        transmissionTextView = convertView.findViewById(R.id.tvTransmission)
        carImageView = convertView.findViewById(R.id.tvImage)
        priceTextView = convertView.findViewById(R.id.tvPrice)


        carNameTextView.text = savedCarList[position].car_name
        driveOptionTextView.text = savedCarList[position].drive
        transmissionTextView.text = savedCarList[position].transmission
        Picasso.get()
            .load(savedCarList[position].front_view)
            .fit()
            .placeholder(R.drawable.vehicle_placeholder)
            .error(R.drawable.vehicle_placeholder)
            .into(carImageView)
        priceTextView.text = "Ksh. " + savedCarList[position].price

        return convertView
    }
}

//class SavedCarAdapter(private val savedCarList: List<SaveCarObject>, val context: Context) : RecyclerView.Adapter<SavedCarAdapter.ViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return ViewHolder(layout)
//    }
//
//    override fun getItemCount(): Int {
//        return savedCarList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindView(savedCarList[position])
//
//        holder.itemView.setOnClickListener {
//            //get position of selected item
//            val savedCar = savedCarList[position]
//            val carId: String? = savedCar.car_id
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
//        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
//        private val imageView: ImageView =itemView.findViewById(R.id.tvImage)
//
//        fun bindView(savedCars: SaveCarObject) {
//            carNameTextView.text = savedCars.car_name
//            driveOptionTextView.text = savedCars.drive
//            transmissionTextView.text = savedCars.transmission
//            Picasso.get().load(savedCars.front_view).into(imageView)
//            priceTextView.text = savedCars.price
//            statusTextView.text = savedCars.status
//        }
//    }
//}