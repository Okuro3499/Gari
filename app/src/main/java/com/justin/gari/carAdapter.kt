package com.justin.gari

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class carAdapter(val arrayList: ArrayList<carModel>, val context: Context) :
    RecyclerView.Adapter<carAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(model: carModel) {
            itemView.tvCarName.text = model.car_name
            itemView.tvDrive.text = model.drive
            itemView.tvTransmission.text = model.transmission
            itemView.tvImage.setImageResource(model.image)
            itemView.tvPrice.text = model.price
            itemView.tvStatus.text = model.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            //get position of selected item
//             val model = arrayList.get(position)
//            get details of selected item with intent
//            var gTitle : String = model.name
//             get image with intent, which position is selected
//            var gImage : Int = model.image

            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}