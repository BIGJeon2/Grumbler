package com.BigJeon.grumbler

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.Dimension
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BigJeon.grumbler.databinding.ActivityMainViewForAppBinding
import com.BigJeon.grumbler.databinding.PostDialogBinding
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

    private lateinit var Post_Text_Container: RelativeLayout
    private lateinit var Finish_Post_Btn: Button

    private lateinit var Add_Text_Container: RelativeLayout
    private lateinit var Get_Content: EditText
    private lateinit var Text_SeekBar: SeekBar
    private lateinit var Text_Color: CircleImageView
    private lateinit var Text_Add_Complete_Btn: Button
    private lateinit var Set_Add_Text_Btn: Button
    private var Text_Size: Float = 15f
    private var Text_Count = 0

    private lateinit var Effect_Imv: ImageView
    private lateinit var Set_Effect_Btn: Button
    private lateinit var Effect_Rcv: RecyclerView
    private var Effect: Int? = null

    private lateinit var Set_Open_Grade: Button
    private lateinit var Grade_Value: String

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

        //파이어 스토어 등록을 위한 변수 선언 - 글 등록 -
        Auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        DataBase = FirebaseFirestore.getInstance()

        //프로필 정보 불러와 Set해줌  - 프로필 정보 -
        Name = post_dialog.findViewById(R.id.Post_My_Name)
        Name.setText(profile.User_Name).toString()
        Img = post_dialog.findViewById(R.id.Post_My_IMG)
        Picasso.get().load(profile.User_Img).into(Img)
        Effect_Imv = post_dialog.findViewById(R.id.Effect_IMV)

        //뷰 참조 모음
        Set_Effect_Btn = post_dialog.findViewById(R.id.Post_Set_Effect_Btn)
        Effect_Rcv = post_dialog.findViewById(R.id.Effect_Rcv)
        Get_Content = post_dialog.findViewById(R.id.Add_Text_EditText)
        Add_Text_Container = post_dialog.findViewById(R.id.Add_Text_Container)
        Set_Add_Text_Btn = post_dialog.findViewById(R.id.Post_Set_Add_Text_Btn)
        Set_Open_Grade = post_dialog.findViewById(R.id.Post_Set_OpenGrade_Btn)
        Set_Open_Grade = post_dialog.findViewById(R.id.Post_Set_OpenGrade_Btn)
        Finish_Post_Btn = post_dialog.findViewById(R.id.Post_Complete_Btn)
        Text_SeekBar = post_dialog.findViewById(R.id.Add_Text_Seek_Bar)
        Text_Color = post_dialog.findViewById(R.id.Add_Text_Color_Set_CIV)
        Text_Add_Complete_Btn = post_dialog.findViewById(R.id.Add_Text_Complete_Btn)
        Post_Text_Container = post_dialog.findViewById(R.id.Post_Text_Container)

        //작성 글에 들어갈 내용 -> 텍스트뷰 3개까지만 제공, 각 텍스트 뷰의 위치(x,y), 색, 크기값 Custom_TextView 데이터 클래스로 저장 - 글 작성 -
        Set_Add_Text_Btn.setOnClickListener {
                Change_Set_View("TEXT")
        }
        //텍스트뷰 동적 추가
        Text_SeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Get_Content.setTextSize(Dimension.DP, seekBar!!.progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(post_dialog.context, "${seekBar!!.progress}", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Get_Content.setTextSize(Dimension.DP, seekBar!!.progress.toFloat())
                Toast.makeText(post_dialog.context, "${seekBar!!.progress}", Toast.LENGTH_SHORT).show()
                Text_Size = seekBar!!.progress.toFloat()
            }
        })
        Text_Add_Complete_Btn.setOnClickListener {
            Add_TextView()
        }

        //이펙트 설정 버튼 클릭시 이펙트RCV 보여주고, 클릭시 해당 이펙트의 Uri 가져와 글라이드로 배경에 삽입 - 이펙트 설정 -
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
                Change_Set_View("EFFECT")
        }

        //공개 범위 설정 - 공개 범위 설정 -
        Grade_Value = Grade_Level_All
        Set_Open_Grade.setOnClickListener {
            Show_Select_Open_Grde_dialog()
        }

        //글 작성 완료시 저장 - 게시 -
        Finish_Post_Btn.setOnClickListener {
            if(Get_Content.text.toString().length > 0){
                Upload_Post()
            }else{
                Toast.makeText(post_dialog.context, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }
        post_dialog.show()
    }
    //공개 범위 설정 다이얼로그 띄우기
    private fun Show_Select_Open_Grde_dialog(){
        val grade_list = arrayOf(Grade_Level_All, Grade_Level_Friends, Grade_Level_My)
        val builder = AlertDialog.Builder(post_dialog.context)
        .setTitle("공개 범위 설정").setItems(grade_list){dialog, which ->
                Toast.makeText(post_dialog.context, "공개 범위 : ${grade_list[which]}", Toast.LENGTH_SHORT).show()
                Grade_Value = grade_list[which]
                Set_Open_Grade.setText(Grade_Value)
            }.show()
    }

    //글 게시하기
    private fun Upload_Post(){
        val Posting_time = SimpleDateFormat("yyyMMddhhmmss").format(Date())
        var Poster_Name = Posting_time + ".${profile.User_UID}"
        var Post = Post_Item(profile, Get_Content.text.toString(), Effect, null, Grade_Value, Posting_time.toString(), null, null, null)
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

    //텍스트뷰 동적 만들기(색, 폰트 크기, 생성 후 뷰 위치 이동 가능, 최대 갯수 3개)
    private fun Add_TextView(){
        if(Text_Count < 3) {
            val textview = TextView(post_dialog.context)
            textview.text = Get_Content.text.toString()
            textview.setTextSize(Dimension.DP, Text_Size)
            textview.setOnClickListener {
                //Delete_View()
            }
            Post_Text_Container.addView(textview)
            Toast.makeText(post_dialog.context, "${Text_Count} / 3", Toast.LENGTH_SHORT).show()
            Text_Count++
            Get_Content.setText("")
        }else{
            Toast.makeText(post_dialog.context, "최대 3개까지 추가 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }
    //텍스트 뷰에 들어갈 텍스트 색 설정해주기 , 배경색 지정 기능 포함
    private fun Set_Text_Color(){

    }
    //버튼 클릭시 설정 컨테이너 전환
    private fun Change_Set_View(State: String){
        when(State){
            "TEXT" -> {
                Effect_Rcv.visibility = View.INVISIBLE
                Add_Text_Container.visibility = View.VISIBLE
            }
            "EFFECT" -> {
                Add_Text_Container.visibility = View.INVISIBLE
                Effect_Rcv.visibility = View.VISIBLE
            }
        }
    }
}