package com.justin.gari.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.justin.gari.R
import com.justin.gari.models.saveCarModels.SaveCarObject

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
//
//            get details of selected item with intent
//            var gTitle : String = model.name
//             get image with intent, which position is selected
//            var gImage : Int = model.image

//            val savedId: String? = savedCar.saved_id

//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("car_id", carId)
//            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carNameTextView: TextView = itemView.findViewById(R.id.tvCarName)
        private val driveOptionTextView: TextView = itemView.findViewById(R.id.tvDriveOption)
        private val transmissionTextView: TextView = itemView.findViewById(R.id.tvTransmission)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)

        fun bindView(savedCars: SaveCarObject) {
            carNameTextView.text = savedCars.car_name
            driveOptionTextView.text = savedCars.drive
            transmissionTextView.text = savedCars.transmission
//            itemView.tvImage.setImageResource(model.image)
            priceTextView.text = savedCars.price
            statusTextView.text = savedCars.status
        }
    }
}