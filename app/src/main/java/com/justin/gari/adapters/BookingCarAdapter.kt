package com.justin.gari.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justin.gari.R
import com.justin.gari.models.bookingCarModels.BookingsResponseObject

class BookingCarAdapter(private val bookedCarList: List<BookingsResponseObject>, val context: Context) : RecyclerView.Adapter<BookingCarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return bookedCarList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(bookedCarList[position])

        holder.itemView.setOnClickListener {
            val bookedCar = bookedCarList[position]
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
        private val driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
        private val transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)

        fun bindView(bookedCars: BookingsResponseObject) {
            carNameTextView.text = bookedCars.car_name
            driveOptionTextView.text = bookedCars.drive
            transmissionTextView.text = bookedCars.transmission
//            itemView.tvImage.setImageResource(model.image)
            priceTextView.text = bookedCars.price
            statusTextView.text = bookedCars.status
        }
    }
}