package com.example.contacts_kotlin


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), ContactsInterface  {
    var showbtn: Button? = null
    var showRecentBtn : Button? = null
    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: ContactAdapter? = null
    var contacts: MutableList<ContactModel>? = null
    var CountTextview : TextView? = null
    var SelectTextview : TextView? = null

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CountTextview = findViewById(R.id.CountTv)
        SelectTextview = findViewById(R.id.SelectTv)

        showbtn = findViewById(R.id.showBtn)
        showRecentBtn= findViewById(R.id.showRecentsbtn)


        showbtn!!.setOnClickListener { requestContactPermission()
            showbtn!!.setVisibility(View.INVISIBLE)}


        showRecentBtn!!.setOnClickListener{
            //val intent = Intent(this@MainActivity, RecentCallLog::class)
            val i = Intent(this,RecentCallLog::class.java)
            startActivity(i)
        }

        recyclerView = findViewById(R.id.recyclerview)
        contacts = ArrayList()
        adapter = ContactAdapter(contacts as ArrayList<ContactModel>, this)
        recyclerView!!.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager


    }

//    public fun CallLog(View view){
//        textView.setText("Call Logging Started ... ");
//        String stringOutput = "";
//
//        Uri uriCallLogs = Uri.parse("content://call_log/calls");
//        Cursor cursorCallLogs = getContentResolver().query(uriCallLogs, null,null,null);
//        cursorCallLogs.moveToFirst();
//        do {
//            String stringNumber = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER));
//            String stringName = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.CACHED_NAME));
//            String stringDuration = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION));
//            String stringType = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE));
//
//            stringOutput = stringOutput + "Number: " + stringNumber
//            + "\nName: " + stringName
//            + "\nDuration: " + stringDuration
//            + "\n Type: " + stringType
//            + "\n\n";
//        }while (cursorCallLogs.moveToNext());
//        textView.setText(stringOutput);
//    }


//   private fun CallLog(){
//
//
////       val projection = arrayOf(
////           CallLog.Calls.CACHED_NAME,
////           CallLog.Calls.NUMBER,
////           CallLog.Calls.TYPE,
////           CallLog.Calls.DATE
////       )
////
////
////        val allCalls: Uri = Uri.parse("content://call_log/calls")
////        val c: Cursor = managedQuery(allCalls, null, null, null, null)
////    //   val phones= contentResolver.query( CallLog.Calls.CONTENT_URI,null, null,null, null);
////
////        val num: String = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)) // for  number
////
////        val name: String = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)) // for name
////
////        val duration: String = c.getString(c.getColumnIndex(CallLog.Calls.DURATION)) // for duration
////
////        val type: Int = c.getString(c.getColumnIndex(CallLog.Calls.TYPE)).toInt()
//    }

    override fun countContacts(count: Int) {

        Log.d("count", "coming")
        CountTextview!!.setVisibility(View.VISIBLE)
        SelectTextview!!.setVisibility(View.VISIBLE)
        CountTextview!!.text= count.toString()


    }

    private fun requestContactPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener { requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            ) }
            builder.show()

        } else {
            getContacts()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }



    private fun getContacts() {

        val phones= contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (phones!!.moveToNext()) {
            val name: String =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber: String =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contacts!!.add(ContactModel(name, phoneNumber))
        }
        adapter!!.notifyDataSetChanged()
        phones.close()
    }
    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }
}