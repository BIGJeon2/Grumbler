package com.BigJeon.grumbler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Effect_Rcv_Adapter(val context: Context, val EffectItemClick: (Effect_Item) -> Unit)
    : RecyclerView.Adapter<Effect_Rcv_Adapter.ViewHolder>() {

    var Effects = arrayListOf<Effect_Item>(
        Effect_Item(R.drawable.edge_round_white, "없음"),
        Effect_Item(R.raw.post_background_effect, "1번"),
        Effect_Item(R.raw.multi_colred_motion, "2번"),
        Effect_Item(R.raw.waliking_rope, "3번"))

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val Effect_Img: ImageView = itemView.findViewById(R.id.Effect_Item_IMV)
        val Effect_Name: TextView = itemView.findViewById(R.id.Effect_Item_Name)
        fun bind(effect: Effect_Item){
            Effect_Name.setText(effect.Effect_Name).toString()
            Glide.with(context).load(effect.Effect).into(Effect_Img)

            itemView.setOnClickListener {
                EffectItemClick(effect)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Effect_Rcv_Adapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.effect_rcv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Effect_Rcv_Adapter.ViewHolder, position: Int) {
        holder.bind(Effects[position])
    }

    override fun getItemCount(): Int = Effects.size
}