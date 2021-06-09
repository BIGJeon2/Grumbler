package com.BigJeon.grumbler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.util.*

class Post_Rcv_Adapter(val context: Context, var Posts: MutableList<Post_Item>, val PostItemClick: (Post_Item) -> Unit, val PostItemLongClick: (Post_Item) -> Unit)
    : RecyclerView.Adapter<Post_Rcv_Adapter.ViewHolder>(){

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        //textview 추가해야함
        val Post_User_Name: TextView = itemView.findViewById(R.id.Post_My_Name)
        val Post_User_Img: CircleImageView = itemView.findViewById(R.id.Post_My_IMG)
        val Post_Effect: ImageView = itemView.findViewById(R.id.Effect_IMV)
        val Post_Favorite_Btn: CircleImageView = itemView.findViewById(R.id.Post_Favorite_Btn)
        val Post_Date: TextView = itemView.findViewById(R.id.Post_Date)
        val Post_Content: TextView = itemView.findViewById(R.id.Post_Content_TextView)
        fun bind(post: Post_Item){

            val Posted_User_UID: String = post.My_Profile.My_UID.toString()
            val Posted_User_Effect: Int? = post.Effect
            val Posted_Grade: String? = post.Grade


            Post_User_Name.setText(post.My_Profile.My_Name).toString()
            Picasso.get().load(post.My_Profile.My_Img).into(Post_User_Img)
            Post_Date.setText(post.Date).toString()
            Glide.with(context).load(post.Effect).into(Post_Effect)
            Post_Content.setText(post.Content.Content.toString())
            Post_Content.setTextSize(post.Content.Size)
            Post_Content.setTextColor(post.Content.Color)
            //Post_Content.fotFeatureSettings 폰트 설정
            //텍스트 뒤 배경색 설정해줘야함
            itemView.setOnClickListener {
                PostItemClick(post)
            }

            itemView.setOnLongClickListener {
                PostItemLongClick(post)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Post_Rcv_Adapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_rcv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Post_Rcv_Adapter.ViewHolder, position: Int) {
        holder.bind(Posts[position])
    }

    override fun getItemCount(): Int = Posts.size
}