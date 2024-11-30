package com.example.chatapplication

import android.content.Context
import android.content.SharedPreferences

// 用于处理用户登录状态的工具类
class CurrentUser(context: Context) {

    val sharedPref: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


    // 保存当前登录的用户ID
    fun saveLoggedInUser(userId: String) {
        val editor = sharedPref.edit()
        editor.putString("loggedInUserId", userId)
        editor.apply()  // 使用 apply() 异步提交数据
    }

    // 获取当前登录的用户ID
    fun getLoggedUserId(): String? {
        return sharedPref.getString("loggedInUserId", null)  // 如果没有登录返回 null
    }

    // 清除登录状态
    fun logoutUser() {
        val editor = sharedPref.edit()
        editor.remove("loggedInUserId")
        editor.apply()
    }
}
