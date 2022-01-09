package com.justin.gari

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(val carList: List<Cars>,) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(carList[position])

        holder.itemView.setOnClickListener {
            //get position of selected item
//             val model = arrayList.get(position)
//            get details of selected item with intent
//            var gTitle : String = model.name
//             get image with intent, which position is selected
//            var gImage : Int = model.image

//            val intent = Intent(context, DetailActivity::class.java)
//            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
        private val driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
        private val transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)

        fun bindView(cars: Cars) {
            carNameTextView.text = cars.car_name
            driveOptionTextView.text = cars.engine
            transmissionTextView.text = cars.transmission
//            itemView.tvImage.setImageResource(model.image)
            priceTextView.text = cars.price.toString()
            statusTextView.text = cars.status
        }
    }
}


//class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    var carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
//    var driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
//    var transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
//    var priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
//    var statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
//}
//
//override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    val cars = arrayList[position]
//    holder.carNameTextView.text = cars.car_name
//    holder.driveOptionTextView.text = cars.engine
//    holder.transmissionTextView.text = cars.transmission
//    holder.priceTextView.text = cars.price.toString()
//    holder.statusTextView.text = cars.status