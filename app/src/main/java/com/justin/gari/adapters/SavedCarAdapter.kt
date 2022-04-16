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
import com.justin.gari.models.saveCarModels.SaveCarObject
import com.squareup.picasso.Picasso

class SavedCarAdapter(private val savedCarList: List<SaveCarObject>, val context: Context) : RecyclerView.Adapter<SavedCarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return savedCarList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(savedCarList[position])

        holder.itemView.setOnClickListener {
            //get position of selected item
            val savedCar = savedCarList[position]
            val carId: String? = savedCar.car_id

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

        fun bindView(savedCars: SaveCarObject) {
            carNameTextView.text = savedCars.car_name
            driveOptionTextView.text = savedCars.drive
            transmissionTextView.text = savedCars.transmission
            Picasso.get().load(savedCars.front_view).into(imageView)
            priceTextView.text = savedCars.price
            statusTextView.text = savedCars.status
        }
    }
}