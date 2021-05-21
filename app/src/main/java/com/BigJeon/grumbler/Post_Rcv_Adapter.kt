package com.BigJeon.grumbler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.util.*

class Post_Rcv_Adapter(val context: Context, var Posts: MutableList<Post_Item>, val PostItemClick: (Post_Item) -> Unit, val PostItemLongClick: (Post_Item) -> Unit)
    : RecyclerView.Adapter<Post_Rcv_Adapter.ViewHolder>(){
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val Post_User_Name: TextView = itemView.findViewById(R.id.Posted_User_Name)
        val Post_User_Img: CircleImageView = itemView.findViewById(R.id.Posted_User_Img)
        val Post_User_Content: TextView = itemView.findViewById(R.id.Posted_Text)
        val Post_Date: TextView = itemView.findViewById(R.id.Posted_Date)
        fun bind(post: Post_Item){
            val Posted_User_UID: String = post.My_Profile.User_UID.toString()
            val Posted_User_Effect: Int? = post.Effect
            val Posted_Text_Color: String? = post.Text_Color
            val Posted_Grade: String? = post.Grade
            Post_User_Name.setText(post.My_Profile.User_Name).toString()
            Picasso.get().load(post.My_Profile.User_Img).into(Post_User_Img)
            Post_User_Content.setText(post.Content).toString()
            Post_Date.setText(post.Posting_Date).toString()

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