package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import java.io.File

class Login : AppCompatActivity() {

    private lateinit var  edtEmail: EditText
    private lateinit var  edtPassword: EditText
    private lateinit var  btnLogin: Button
    private lateinit var  btnSignUp: Button
    private lateinit var  currentuser: CurrentUser
    private lateinit var applog: ImageView
    private lateinit var messagelist: ArrayList<Message>

    private  lateinit var db:Database



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        actionBar?.hide()

        db = Database(this)
        currentuser = CurrentUser(this)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_Login)
        btnSignUp = findViewById(R.id.btn_Signup)
        applog = findViewById(R.id.app_logo)




        btnLogin.setOnClickListener(){
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
            } else {
                val isValid = db.checkUser(email, password)

                if (isValid) {
                    // 跳转到主界面或欢迎界面
                    val intent = Intent(this, MainActivity::class.java)
                    currentuser.saveLoggedInUser(db.getNameById(email)!!)
                    Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()

        }



    }


}