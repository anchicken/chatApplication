package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUp : AppCompatActivity() {

    private lateinit var  edtEmail: EditText
    private lateinit var  edtPassword: EditText
    private lateinit var  btnSignUp: Button
    private lateinit var edtName:EditText

    private  lateinit var db:Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        actionBar?.hide()

        db = Database(this)

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtName = findViewById(R.id.edt_name)
        btnSignUp = findViewById(R.id.btn_Signup)

        btnSignUp.setOnClickListener(){
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val username = edtName.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show()
            } else {
                if (db.checkUsername(email)) {
                    Toast.makeText(this, "用户名已存在，请选择其他用户名", Toast.LENGTH_SHORT).show()
                } else {
                    val success = db.addUser(email, username, password)
                    if (success) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                        // 跳转到登录界面
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}