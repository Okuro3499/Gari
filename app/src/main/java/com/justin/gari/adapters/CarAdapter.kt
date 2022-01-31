package com.justin.gari.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justin.gari.R
import com.justin.gari.activities.DetailActivity
import com.justin.gari.models.carModels.Cars
import com.squareup.picasso.Picasso

class CarAdapter(private val carList: List<Cars>, val context: Context) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>() {
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
//
//            get details of selected item with intent
//            var gTitle : String = model.name
//             get image with intent, which position is selected
//            var gImage : Int = model.image

            val carId: String? = car.car_id

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("car_id", carId)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
        private val driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
        private val transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val imageView: ImageView =itemView.findViewById(R.id.tvImage)

        fun bindView(cars: Cars) {
            carNameTextView.text = cars.car_name
            driveOptionTextView.text = cars.drive
            transmissionTextView.text = cars.transmission
            Picasso.get().load(cars.front_view).into(imageView);
//            itemView.tvImage.setImageResource(model.image)
            priceTextView.text = cars.price
            statusTextView.text = cars.status
        }
    }
}