package com.BigJeon.grumbler

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.BigJeon.grumbler.databinding.FragmentMyPostListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class My_Post_List_Fragment(val My_Uid: String) : Fragment(R.layout.fragment_my__post__list_) {

    private lateinit var Auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var DataBase: FirebaseFirestore

    private var Posts = mutableListOf<Post_Item>()
    private lateinit var Adapter: Post_Rcv_Adapter

    private var Fragment_binding: FragmentMyPostListBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMyPostListBinding.bind(view)
        Fragment_binding = binding

        Auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        DataBase = FirebaseFirestore.getInstance()
        //Rcv 설정
        val Mine_Rcv = binding.PostingListMine
        Adapter = Post_Rcv_Adapter(view.context, Posts, { post ->
            Toast.makeText(context, "어뎁터 온클릭 확인", Toast.LENGTH_SHORT).show()
        },{post ->
            Toast.makeText(context, "어뎁터 클릭 확인", Toast.LENGTH_SHORT).show()
        })
        val layout_manager = LinearLayoutManager(this.context)
        Mine_Rcv.adapter = Adapter
        Mine_Rcv.layoutManager = layout_manager
        Mine_Rcv.setHasFixedSize(true)
        println(My_Uid)
        Get_Mine_Post()

        //리스트 페이징
        binding.RefreshRCV.setOnRefreshListener {
            Posts.clear()
            Get_Mine_Post()
            Adapter.notifyDataSetChanged()
            binding.RefreshRCV.isRefreshing = false
        }
    }

    //내가 쓴 포스팅 리스트 모두 갖고옴(20개로 수정)
    private fun Get_Mine_Post(){
        DataBase.collection("Posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val getting_post = document.toObject(Post_Item::class.java)
                    if(getting_post.My_Profile.User_UID == My_Uid) {
                        Posts.add(0, getting_post)
                        Adapter.notifyDataSetChanged()
                        println(getting_post)
                    }
                }
            }
    }

    override fun onDestroy() {
        Fragment_binding = null
        super.onDestroy()
    }
}