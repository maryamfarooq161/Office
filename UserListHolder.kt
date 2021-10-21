package com.example.contacts_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListHolder(v: View) : RecyclerView.ViewHolder(v) {
    var name : TextView? = null
    var num : TextView? = null
    init {
        name = v.findViewById<View>(R.id.name) as TextView
        num = v.findViewById<View>(R.id.num) as TextView
        v.isClickable = false
    }
}