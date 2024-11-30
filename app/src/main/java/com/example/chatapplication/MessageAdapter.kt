package com.example.chatapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlin.math.sign

class MessageAdapter(val context: Context, val messagelist: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SENT = 2;
    private val sessionManager = CurrentUser(context)
    private val loggedInUserId: String? = sessionManager.getLoggedUserId()
    private lateinit var image: ImageView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent,false)
            return  ReceiveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent,false)
            return  SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messagelist[position]

        if(holder.javaClass == SentViewHolder::class.java){

            val viewHolder = holder as SentViewHolder

            if (!currentMessage.imagepath.isNullOrEmpty()) {

                Glide.with(context)
                    .load(currentMessage.imagepath)  // 这里是图片的路径或 URL
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // 禁用缓存
                    .skipMemoryCache(true)  // 禁用内存缓存
                    .into(holder.messageImage)

                holder.sentName.text = currentMessage.sendername
                holder.sentMessage.text = currentMessage.message
            }else{

                holder.messageImage.setImageURI(null)
                holder.sentName.text = currentMessage.sendername
                holder.sentMessage.setBackgroundResource(R.drawable.sent_messagerbox_background)
                holder.sentMessage.setPadding(10,10,10,10)
                holder.sentMessage.text = currentMessage.message
            }
        }
        else{
            val viewHolder = holder as ReceiveViewHolder
            if (!currentMessage.imagepath.isNullOrEmpty()) {
                Glide.with(context)
                    .load(currentMessage.imagepath)  // 这里是图片的路径或 URL
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // 禁用缓存
                    .skipMemoryCache(true)  // 禁用内存缓存
                    .into(holder.messageImage)

                holder.receiveName.text = currentMessage.sendername
                holder.receiveMessage.text = currentMessage.message
            }else{
                holder.messageImage.setImageURI(null)
                holder.receiveName.text = currentMessage.sendername
                holder.receiveMessage.setBackgroundResource(R.drawable.receive_messagebox_background)
                holder.receiveMessage.setPadding(10,10,10,10)
                holder.receiveMessage.text = currentMessage.message
            }
        }
    }



    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagelist[position]

        if(loggedInUserId.equals(currentMessage.sendername)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentName = itemView.findViewById<TextView>(R.id.txt_sent_name)
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
        var messageImage = itemView.findViewById<ImageView>(R.id.sent_messageImage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receiveName = itemView.findViewById<TextView>(R.id.txt_receive_name)
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
        var messageImage = itemView.findViewById<ImageView>(R.id.receive_messageImage)
    }
}