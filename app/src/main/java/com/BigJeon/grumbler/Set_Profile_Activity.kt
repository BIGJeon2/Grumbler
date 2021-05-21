package com.BigJeon.grumbler

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.BigJeon.grumbler.databinding.ActivitySetProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class Set_Profile_Activity : AppCompatActivity() {

    private var User_Img: String? = null
    private var User_Name: String? = null
    private var User_Uid: String? = null
    private var Img_uri: Uri? = null

    private lateinit var Auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var DataBase: FirebaseFirestore

    val Get_Image = 1001

    private lateinit var binding: ActivitySetProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        DataBase = FirebaseFirestore.getInstance()

        User_Uid = Auth.currentUser.uid
        Toast.makeText(this, "$User_Uid.", Toast.LENGTH_SHORT).show()
        loadData()

        //갤러리에서 이미지 불러오기
        binding.UserProfileIMG.setOnClickListener {
            IMG_Clicked()
        }
        //유저 프로필 저장후 엑티비티 이동
        binding.SetProfileCompleteBTN.setOnClickListener {
            if(binding.UserProfileName.text.toString() == null || Img_uri == null){
                Toast.makeText(this, "사용하실 이름과 사진을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                Add_User()
            }
        }
    }


    //이미지 뷰 클릭시 갤러리 사진 갖고오기.
    private fun IMG_Clicked(){
        val Select_Img = Intent(Intent.ACTION_PICK)
        Select_Img.setType("image/*")
        startActivityForResult(Select_Img, Get_Image)
    }


    //사진 선택 완료시 사진 로드하여 이미지뷰에 적용
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Get_Image){
            if(resultCode == RESULT_OK){
                Img_uri = data?.data
                Picasso.get().load(Img_uri).into(binding.UserProfileIMG)
            }
        }
    }


    //FireStore에 유저 정보 저장
    private fun Add_User(){
        var fileName = SimpleDateFormat("yyyMMddhhmmss").format(Date()) + ".png"
        var storage_reference = storage.getReference("Profile_Images/$fileName")
        var uploadTask = storage_reference.putFile(Img_uri!!)
        uploadTask.addOnSuccessListener {
            storage_reference.downloadUrl.addOnSuccessListener { uri ->
                User_Img = uri.toString()
                User_Name = binding.UserProfileName.text.toString()
                var My_Profile = User(User_Uid, User_Name, User_Img)
                Upload_to_FireStore(My_Profile)
                Toast.makeText(this, "프로필 저장 완료", Toast.LENGTH_SHORT).show()
                Go_Main_View(My_Profile)
                finish()
            }
        }
    }


    //FireSotre에 Upload
    private fun Upload_to_FireStore(user: User){
        DataBase.collection("Users").document("$User_Uid")
            .set(user)
            .addOnSuccessListener { DocumentReference ->
                Toast.makeText(this, "db저장완료", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "db저장실패", Toast.LENGTH_SHORT).show()
            }
    }


    //기존 회원일시 프로필 정보 가져오기
    private fun loadData(){
        DataBase.collection("Users")
            .whereEqualTo("user_UID", User_Uid)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                val My_Profile  = document.toObject(User::class.java)
                binding.UserProfileName.setText(My_Profile.User_Name).toString()
                Picasso.get().load(My_Profile.User_Name).into(binding.UserProfileIMG)
                Go_Main_View(My_Profile)
                finish()
                }
            }
    }

    //메인뷰로 이동
    private fun Go_Main_View(profile: User){
        val go_App_Main = Intent(this@Set_Profile_Activity, Main_View_For_App::class.java)
        go_App_Main.putExtra("Name", profile.User_Name)
        go_App_Main.putExtra("Uid", profile.User_UID)
        go_App_Main.putExtra("Img", profile.User_Img)
        startActivity(go_App_Main)
    }
}
