package com.BigJeon.grumbler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.BigJeon.grumbler.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var googleSignInClient: GoogleSignInClient? = null
    private val Sign_In_Request_Code = 1001
    private val TAG = "GooGle_Login"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.GooGleLoginBtn.setOnClickListener {
            val SignIn_Intent = googleSignInClient?.signInIntent
            startActivityForResult(SignIn_Intent, Sign_In_Request_Code)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Sign_In_Request_Code){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Google_Firebase_Login" + account.id)
                FireBaseAuthWithGoogle(account)
                val Intent_Set_Profile = Intent(this, Set_Profile_Activity::class.java)
                startActivity(Intent_Set_Profile)
            }catch (e: ApiException){
                Log.w(TAG, "LOGIN_FAILED", e)
            }
        }
    }
    private fun FireBaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                }else{
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}
