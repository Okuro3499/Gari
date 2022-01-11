package com.justin.gari.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.justin.gari.R
import com.justin.gari.activities.DetailActivity
import com.justin.gari.models.Cars

class CarAdapter(private val carList: List<Cars>, val context: Context) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
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
            val car = carList[position]
//            Toast.makeText("clicked $car",Toast.LENGTH_SHORT).show
//            get details of selected item with intent
//            var gTitle : String = model.name
//             get image with intent, which position is selected
//            var gImage : Int = model.image
            
            val carId: String? = car.car_id
            val carName: String? = car.car_name
            val status: String? = car.status
            val transmission: String? = car.transmission
            val engine: String? = car.engine
            val color: String? = car.color
            val registration: String? = car.registration
            val passengers: String? = car.passengers
            val company: String? = car.company
            val price: String? = car.price
            val doors: String? = car.doors

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("car_id", carId)
            intent.putExtra("car_name", carName)
            intent.putExtra("status", status)
            intent.putExtra("transmission", transmission)
            intent.putExtra("engine", engine)
            intent.putExtra("color", color)
            intent.putExtra("registration", registration)
            intent.putExtra("passengers", passengers)
            intent.putExtra("company", company)
            intent.putExtra("price", price)
            intent.putExtra("doors", doors)
            context.startActivity(intent)
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
            priceTextView.text = cars.price
            statusTextView.text = cars.status
        }
    }
}