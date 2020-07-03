package com.hack3r.amshel.mutall.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hack3r.amshel.mutall.R
import com.hack3r.amshel.mutall.models.MessageObject

class SmsAdapter(val context: Context, val messages:List<MessageObject>) : BaseAdapter(){

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val messageView : View
        var holder: ViewHolder

        if (convertView == null){
            messageView = LayoutInflater.from(context).inflate(R.layout.single_message, parent, false)
            holder = ViewHolder()
            holder.number = messageView.findViewById(R.id.num)
            holder.message = messageView.findViewById(R.id.msg)

            messageView.tag = holder
        }else{
            holder = convertView.tag as ViewHolder
            messageView = convertView
        }
        var msgObject = messages[position];
        holder.message?.text = msgObject.message
        holder.number?.text = msgObject.number

        return messageView
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getCount(): Int {
        return messages.count()
    }

    private class ViewHolder{
        var number: TextView? = null
        var message : TextView? = null
    }
}