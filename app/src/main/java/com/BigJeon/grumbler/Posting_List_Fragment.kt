package com.BigJeon.grumbler

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.BigJeon.grumbler.databinding.FragmentPostingListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Posting_List_Fragment : Fragment(R.layout.fragment_posting__list_) {

    private lateinit var Auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var DataBase: FirebaseFirestore
    
    private var Fragment_binding: FragmentPostingListBinding? = null
    private var Posts = mutableListOf<Post_Item>()
    private lateinit var Adapter: Post_Rcv_Adapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPostingListBinding.bind(view)
        Fragment_binding = binding

        Auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        DataBase = FirebaseFirestore.getInstance()

        Get_All_Post()

        val post_rcv = binding.PostingList
        Adapter = Post_Rcv_Adapter(view.context, Posts, { post ->
                Toast.makeText(context, "어뎁터 온클릭 확인", Toast.LENGTH_SHORT).show()
            },{post ->
                Toast.makeText(context, "어뎁터 클릭 확인", Toast.LENGTH_SHORT).show()
            })
        //RCV역순으로 표시(최신순으로 배치), 상단 최대 스크롤시 리스트 클리어후 최신 포스터 가져오
        val layout_manager = LinearLayoutManager(this.context)
        post_rcv.adapter = Adapter
        post_rcv.layoutManager = layout_manager
        post_rcv.setHasFixedSize(true)

        //리스트 페이징
        binding.RefreshRCV.setOnRefreshListener {
            Posts.clear()
            Get_All_Post()
            Adapter.notifyDataSetChanged()
            binding.RefreshRCV.isRefreshing = false
        }
    }
    //현재있는 리스트 모두 갖고옴(50개로 수정)
    private fun Get_All_Post(){
        DataBase.collection("Posts")
            .whereEqualTo("grade", "All_User")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val getting_post = document.toObject(Post_Item::class.java)
                    Posts.add(0, getting_post)
                    Adapter.notifyDataSetChanged()
                }
            }
        }
    override fun onDestroyView() {
        Fragment_binding = null
        super.onDestroyView()
    }
}
