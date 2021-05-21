package com.BigJeon.grumbler

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class Alert_Post_Dialog(context: Context, My_Profile: User) {
    private val post_dialog = Dialog(context)
    private lateinit var Name: TextView
    private lateinit var Img: CircleImageView
    private lateinit var Finish_Post_Btn: Button
    private lateinit var Get_Content: EditText
    private lateinit var Effect_Imv: ImageView
    private var Effect: Int? = null
    private lateinit var Set_Effect_Btn: Button
    private lateinit var Set_Open_Grade: Button
    private lateinit var Grade_Value: String
    private lateinit var Img_In_Post: String
    private lateinit var Effect_Rcv: RecyclerView

    private lateinit var Auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var DataBase: FirebaseFirestore

    private val Grade_Level_All = "All_User"
    private val Grade_Level_Friends = "For_Friends"
    private val Grade_Level_My = "Only_Me"

    val profile = My_Profile
    fun start(){
        post_dialog.setContentView(R.layout.post_dialog)
        post_dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        post_dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        post_dialog.setCanceledOnTouchOutside(true)
        post_dialog.setCancelable(true)
        //프로필 정보 불러와 Set해줌
        Name = post_dialog.findViewById(R.id.Post_My_Name)
        Name.setText(profile.User_Name).toString()
        Img = post_dialog.findViewById(R.id.Post_My_IMG)
        Picasso.get().load(profile.User_Img).into(Img)
        //작성 글에 들어갈 내용
        Effect_Imv = post_dialog.findViewById(R.id.Effect_IMV)
        Get_Content = post_dialog.findViewById(R.id.Post_Text_EditText)
        //이펙트 설정 버튼 클릭시 이펙트RCV 보여주고, 클릭시 해당 이펙트의 Uri 가져와 글라이드로 배경에 삽입
        //Rcv_Set
        Set_Effect_Btn = post_dialog.findViewById(R.id.Post_Set_Effect_Btn)
        Effect_Rcv = post_dialog.findViewById(R.id.Effect_Rcv)
        val layoutManager = LinearLayoutManager(post_dialog.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val Adapter = Effect_Rcv_Adapter(post_dialog.context, {Effect_item ->
            Glide.with(post_dialog.context).load(Effect_item.Effect).into(Effect_Imv)
            Effect = Effect_item.Effect
        })
        Effect_Rcv.adapter = Adapter
        Effect_Rcv.layoutManager = layoutManager
        Effect_Rcv.setHasFixedSize(true)
        Adapter.notifyDataSetChanged()
        Set_Effect_Btn.setOnClickListener {
            Effect_Rcv.visibility = View.VISIBLE
        }

        //공개 범위 설정
        Set_Open_Grade = post_dialog.findViewById(R.id.Post_Set_OpenGrade_Btn)
        Grade_Value = Grade_Level_All
        Set_Open_Grade.setOnClickListener {
            Show_Select_Open_Grde_dialog()
        }

        //파이어 스토어 등록을 위한 변수 선언
        Auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        DataBase = FirebaseFirestore.getInstance()

        Finish_Post_Btn = post_dialog.findViewById(R.id.Post_Complete_Btn)
        Finish_Post_Btn.setOnClickListener {
            if(Get_Content.text.toString().length > 0){
                Upload_Post()
            }else{
                Toast.makeText(post_dialog.context, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }
        post_dialog.show()
    }

    private fun Show_Select_Open_Grde_dialog(){
        val grade_list = arrayOf(Grade_Level_All, Grade_Level_Friends, Grade_Level_My)
        val builder = AlertDialog.Builder(post_dialog.context)
        .setTitle("공개 범위 설정").setItems(grade_list){dialog, which ->
                Toast.makeText(post_dialog.context, "공개 범위 : ${grade_list[which]}", Toast.LENGTH_SHORT).show()
                Grade_Value = grade_list[which]
                Set_Open_Grade.setText(Grade_Value)
            }.show()
    }
    private fun Upload_Post(){
        val Posting_time = SimpleDateFormat("yyyMMddhhmmss").format(Date())
        var Poster_Name = Posting_time + ".${profile.User_UID}"
        var Post = Post_Item(profile, Get_Content.text.toString(), Effect, null, Grade_Value, Posting_time.toString())
        DataBase.collection("Posts").document("$Poster_Name")
            .set(Post)
            .addOnSuccessListener { DocumentReference ->
                Toast.makeText(post_dialog.context, "글이 정상적으로 등록되었습니다.", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(post_dialog.context, "글등록을 실패 하였습니다.", Toast.LENGTH_SHORT).show()
            }
        post_dialog.dismiss()
    }
}