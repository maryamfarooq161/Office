package com.example.contacts_kotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_user_list_holder.*

class ContactAdapter : RecyclerView.Adapter<UserListHolder> {

    var count : Int = 0
    var selectMode : Boolean = false
    private var selectedcontacts : List<ContactModel>?= null

    private var contactList : List<ContactModel>?= null

    var contactinterfaceObj : ContactsInterface? =null
    private var mContext: Context? = null

    constructor(contactList: List<ContactModel>, contactinterfaceObj: ContactsInterface){
        this.contactList=contactList;
        this.contactinterfaceObj=contactinterfaceObj
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.activity_user_list_holder, null)
        //View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,null);
        return UserListHolder(v)
    }

    override fun getItemCount(): Int {

        return contactList!!.size
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {

        val Contactitems = contactList!![position]
        val contactname = holder.name
        val contactnum = holder.num
       contactname!!.text=Contactitems.name
        contactnum!!.text=Contactitems.phoneNo

    holder.itemView.setOnClickListener(){
        Log.d("count1", "coming")
        count++;
        contactinterfaceObj?.countContacts(count)
    }
//        holder.itemView.setOnLongClickListener(){
//            selectMode = true
////            if
//            false
//        }
    }

}