package com.example.contacts_kotlin

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract.PhoneLookup
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class RecentCallLog : AppCompatActivity() {
    var showRecentBtn : Button? = null
    var calllogtextView : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_call_log)

        calllogtextView=findViewById(R.id.calltv)
        showRecentBtn= findViewById(R.id.showRecentsbtn)
        showRecentBtn!!.setOnClickListener { requestCallLogPermission()
            showRecentBtn!!.setVisibility(View.INVISIBLE)}

    }

    private fun requestCallLogPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                android.Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED)
            {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CALL_LOG),
                PERMISSIONS_REQUEST_READ_CALL_LOG
            )
            //callback onRequestPermissionsResult
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener { requestPermissions(
                arrayOf(android.Manifest.permission.READ_CALL_LOG),
                PERMISSIONS_REQUEST_READ_CALL_LOG
            ) }
            builder.show()

        } else {
            getRecentContacts()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecentCallLog.PERMISSIONS_REQUEST_READ_CALL_LOG) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getRecentContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getRecentContacts() {
        val sb = StringBuffer()
        val NameHolder: MutableList<String> = ArrayList()
        var Name: String? = null
        val phones= contentResolver.query(
            CallLog.Calls.CONTENT_URI, null, null, null, null
        )
//        val name: String = phones!!.getString(phones.getColumnIndex(CallLog.Calls.CACHED_NAME))
        val names: Int = phones!!.getColumnIndex(CallLog.Calls.CACHED_NAME)

        val num: Int = phones!!.getColumnIndex(CallLog.Calls.NUMBER)
        val type: Int = phones!!.getColumnIndex(CallLog.Calls.TYPE)
        val date: Int = phones!!.getColumnIndex(CallLog.Calls.DATE)
        val duration: Int = phones!!.getColumnIndex(CallLog.Calls.DURATION)
        sb.append("Call Details\n\n")
        while (phones!!.moveToNext()) {
            val number: String = phones.getString(num)
//            val name : String?= getContactName(number)

            val name: String? = phones.getString(names)
            if (name == null) {
                Name = "Unknown"
                NameHolder.add(Name)
            } else {
                Name = name
                NameHolder.add(Name)
            }

            val type: String = phones.getString(type)
//            val name: String = phones.getString(names)
            val callDate: String = phones.getString(date)
            val callDayTime = Date(java.lang.Long.valueOf(callDate))
            val callDuration: String = phones.getString(duration)
//            contacts!!.add(ContactModel(name, phoneNumber))

            var dir : String? = null
            var callcode : Int = Integer.parseInt(type);
            when(callcode)
            {
                CallLog.Calls.OUTGOING_TYPE -> dir = "Outgoing"
                CallLog.Calls.INCOMING_TYPE -> dir = "Incoming";
                CallLog.Calls.MISSED_TYPE -> dir = "Missed"
            }
            sb.append(
                "\nName:--- " + name + "\nPhone Number:--- " + number + " \nCall Type:--- "
                        + dir + " \nCall Date:--- " + callDayTime
                        + " \nCall duration in sec :--- " + callDuration
            );
            sb.append("\n----------------------------------");
        }


        phones.close();
        calllogtextView?.setText(sb);
        }

    fun getContactName(phoneNumber: String?): String? {
//
        val uri: Uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor = getContentResolver().query(
            uri, arrayOf(PhoneLookup.DISPLAY_NAME),
            null, null, null
        ) ?: return null
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        return contactName
    }


    companion object {
        const val PERMISSIONS_REQUEST_READ_CALL_LOG = 1
    }
}