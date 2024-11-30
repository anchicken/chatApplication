package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

public class Database(context: Context)  :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDB.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_USEREMAIL = "useremail"
        private const val COLUMN_PASSWORD = "password"

        private const val TABLE_MESSAGEs = "Messages"
        private const val COLUMN_MESSAGE = "Message"
        private const val COLUMN_IMAGE_PATH = "image_path"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USEREMAIL TEXT UNIQUE, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createUsersTable)

        val createMessagesTable = ("CREATE TABLE $TABLE_MESSAGEs ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_MESSAGE TEXT,"
                + "$COLUMN_IMAGE_PATH TEXT)")
        db?.execSQL(createMessagesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 简单起见，直接删除旧表并创建新表
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGEs")
        onCreate(db)
    }

    // 插入新用户
    fun addUser(email: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USEREMAIL, email)
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun addMessage(username: String, message: String, imagePath: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_MESSAGE, message)
            put(COLUMN_IMAGE_PATH, imagePath)
        }

        return try {
            val result = db.insert(TABLE_MESSAGEs, null, values)
            result != -1L
        } catch (e: Exception) {
            e.printStackTrace() // 处理异常
            false
        } finally {
        db.close()
        }
    }

    // 检查用户是否存在
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USEREMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // 检查用户名是否已存在
    fun checkUsername(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USEREMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }


    @SuppressLint("Range")
    fun getNameById(email: String): String? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var name: String? = null

        try {
            val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USEREMAIL = ?"
            cursor = db.rawQuery(query, arrayOf(email))

            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return name
    }
}

