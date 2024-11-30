package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TABLE_MESSAGEs = "Messages"
        private const val COLUMN_MESSAGE = "Message"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_IMAGE_PATH = "image_path"
    }

    private lateinit var currentuser: CurrentUser
    private lateinit var chatrecyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var dbHelper: Database
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messagelist: ArrayList<Message>

    private lateinit var rootLayout: RelativeLayout
    private lateinit var editText: EditText




    @SuppressLint("NotifyDataSetChanged", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        // Get ActionBar and customize it
//        supportActionBar?.title = "My App"  // Set the title of the ActionBar
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Show the Up button (back button)



        currentuser = CurrentUser(this)
        chatrecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messagelist = ArrayList()


        dbHelper = Database(this)

        messagelist = getMessagesFromDatabase(dbHelper)

        //  设置适配器
        adapter = MessageAdapter(this, messagelist)

        chatrecyclerView.layoutManager = LinearLayoutManager(this)

        chatrecyclerView.adapter = adapter

        adapter.notifyDataSetChanged()


        sendButton.setOnClickListener(){

            var message = messageBox.text.toString()
            var imagepath = ""

            if (message.isNotEmpty()) {
                if (message.startsWith("image:")) {
                    val updatedString = message.replaceFirst("image:", "")
                    imagepath = updatedString
                    message = ""
                }
            }
//          保存消息到数据库
            dbHelper.addMessage(currentuser.getLoggedUserId()!!, message, imagepath)

            // 清空现有数据并重新加载消息
            messagelist.clear()
            messagelist.addAll(getMessagesFromDatabase(dbHelper))

            // 通知适配器更新
            adapter.notifyDataSetChanged()

            // 清空输入框
            messageBox.setText("")
        }



        // 监听键盘状态
//        val rootView = findViewById<View>(R.id.chatactivity)
//        rootView.viewTreeObserver.addOnPreDrawListener {
//            val rect = Rect()
//            rootView.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = rootView.height
//            val keypadHeight = screenHeight - rect.bottom
//
//            if (keypadHeight > 0) {
//                // 键盘弹出，调整布局
//                adjustLayoutForKeyboard(true)
//            } else {
//                // 键盘隐藏，恢复布局
//                adjustLayoutForKeyboard(false)
//            }
//            true
//        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            val intent = Intent(this@MainActivity, Login::class.java)
            currentuser.logoutUser()
            finish()
            startActivity(intent)
            return true
        }

        return true
    }

    @SuppressLint("Range")
    private fun getMessagesFromDatabase(dbHelper:Database): ArrayList<Message> {
        val messages = ArrayList<Message>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_MESSAGEs", null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
                val message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE))
                val imagepath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH))
                messages.add(Message(message, name, imagepath))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return messages
    }

    private fun adjustLayoutForKeyboard(isKeyboardVisible: Boolean) {
        val editTextHeight = editText.height
        if (isKeyboardVisible) {
            // 如果键盘可见，调整根布局的 padding，避免键盘遮挡
            rootLayout.setPadding(0, 0, 0, editTextHeight) // 留出空白区域
        } else {
            // 如果键盘不可见，恢复原始布局
            rootLayout.setPadding(0, 0, 0, 0)
        }
    }

    private fun updateMessages(dbHelper:Database) {
        val newMessages = getMessagesFromDatabase(dbHelper)
        adapter = MessageAdapter(this, newMessages)  // 更新 RecyclerView 的数据
    }

//    fun isValidImagePath(path: String?): Boolean {
//        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), path!!)
//        Log.d("image","file:${file.exists()}" )
//        if (!file.exists()) {
//            return false
//        }
//
//        // 尝试解码文件为图片
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true // 不加载图片，只获取尺寸信息
//        BitmapFactory.decodeFile(path, options)
//
//        return options.outWidth != -1 && options.outHeight != -1
//    }


}

