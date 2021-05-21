package com.BigJeon.grumbler

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.BigJeon.grumbler.databinding.ActivityMainViewForAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class Main_View_For_App : AppCompatActivity() {

    private lateinit var My_Profile: User

    private var Fragment_Status = "All"
    var Auth = FirebaseAuth.getInstance()
    val fragmentManager = supportFragmentManager

    private lateinit var binding: ActivityMainViewForAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewForAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Load_Profile()
        All_Post_Fragment()
        //모든 글 가져오기
        binding.listAll.setOnClickListener {
            if(Fragment_Status != "All"){
                All_Post_Fragment()
                Fragment_Status = "All"
            }
        }
        //내가 쓴글 프래그먼트 띄우기
        binding.listMine.setOnClickListener {
            if(Fragment_Status != "Mine"){
                My_Post_Fragment()
                Fragment_Status = "Mine"
            }
        }
        //셋팅 액티비티로 이동
        binding.SettingBtn.setOnClickListener {

        }
        //포스팅 다이얼로그 띄우기
        binding.PostStartFloatBtn.setOnClickListener {
            Alert_Custom_Dialog()
        }
    }
    //현재사용자 로그아웃
    private fun Sign_Out(){
        Auth.signOut()
        val Go_Login_Activity = Intent(this, MainActivity::class.java)
        startActivity(Go_Login_Activity)
    }
    //현재 사용자 프로필 불러오기
    private fun Load_Profile(){
        if(intent.hasExtra("Name")) {
            My_Profile = User(intent.getStringExtra("Uid")!!, intent.getStringExtra("Name"), intent.getStringExtra("Img"))
            binding.MyName.setText(My_Profile.User_Name).toString()
            Picasso.get().load(My_Profile.User_Img).into(binding.MyIMG)
        }
    }
    //글작성 버튼 클릭시 글 작성 Dialog띄워주기
    private fun Alert_Custom_Dialog(){
        val Post_Dialog = Alert_Post_Dialog(this, My_Profile)
        Post_Dialog.start()
    }

    private fun All_Post_Fragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Posting_List_Fragment()
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun Firends_Post_List(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Posting_List_Fragment()
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun My_Post_Fragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = My_Post_List_Fragment(My_Uid = My_Profile.User_UID.toString())
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun Friends_List(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Posting_List_Fragment()
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}