package com.hack3r.amshel.mutall

import android.Manifest
import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog
import com.hack3r.amshel.mutall.controllers.Accounts
import com.hack3r.amshel.mutall.models.MessageObject
import com.hack3r.amshel.mutall.utilities.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var urls: Map<String, String>
    lateinit var messageType: Array<String>
    lateinit var recipients: Array<String>
    lateinit var library : Library
    lateinit var inboxMsg: JSONArray
    val SMS_PERMISSIONS_REQUEST:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        library = Library(this)
        messageType = resources.getStringArray(R.array.accounts)
        recipients = resources.getStringArray(R.array.recipients)
        urls = mapOf(messageType[0] to ACCOUNTS_URL, messageType[1] to NOREADING_URL)
        inboxMsg = JSONArray()

        requestSmsPermission()
        retrieveAccounts.setOnClickListener {
            val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.account_type)
            alertDialog.setItems(messageType,
                    DialogInterface.OnClickListener{
                        dialog: DialogInterface?, which: Int ->
                        retrieveAccounts(urls[messageType[which]])
                    })
            alertDialog.create().show()
        }

        inbox.setOnClickListener {
            val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.view_inbox)
            alertDialog.setItems(recipients,
                    DialogInterface.OnClickListener{
                        dialog: DialogInterface?, which: Int ->
                        showInbox(recipients[which])
                    })
            alertDialog.create().show()
        }

    }

    fun retrieveAccounts(url:String?){
        library.showProgress("fetching")
        val jsonArrayRequest  = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener {response ->
                    println(response)
                    library.dismissProgress()
                    AwesomeSuccessDialog(this)
                            .setTitle("Accounts")
                            .setMessage("Send Accounts to kplc OR Review Them")
                            .setColoredCircle(R.color.md_light_blue_600)
                            .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                            .setCancelable(true)
                            .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                            .setPositiveButtonText("REVIEW ACCOUNTS")
                            .setPositiveButtonClick {
                                val intent = Intent(this, Accounts::class.java)
                                intent.putExtra("json", response.toString())
                                intent.putExtra("type", "send")
                                startActivity(intent)
                            }
                            .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setNegativeButtonText("SEND TO KPLC")
                            .setNegativeButtonClick {
                                val activity:Thread = SendSms(response)
                                activity.start()
                            }
                            .show()
                },
                Response.ErrorListener {error: VolleyError ->
                    library.dismissProgress()
                    if(error.networkResponse.statusCode == 500){
                        library.showDialog(this, "error", "SERVER ERROR", "SOMETHING WRONG HAPPENED ON THE SERVER")

                    }
                    error.printStackTrace()})

        VolleyController.instance.addRequestQueue(jsonArrayRequest)
    }


    /**
     * function for requesting sms permission
     * targeted for android api 23 and above
     */
    fun requestSmsPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //permission not granted
            //show an explanation why
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.SEND_SMS)) {
                library.showToast("Please allow permission!", "error")

            } else {

                //request the permission
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSIONS_REQUEST)

            }
        }
    }


    fun showInbox(address: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            run { requestReadRequest() }
        val contentResolver = contentResolver
        val selection = '\''.toString() + address + '\''.toString()
        try {
            val smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, "address= $selection", null, null)
            val indexBody = smsInboxCursor!!.getColumnIndex("body")
            val indexAddress = smsInboxCursor.getColumnIndex("address")
            if (smsInboxCursor.moveToFirst()) {
                do {
                    val smsNum = smsInboxCursor.getString(indexAddress)
                    val smsBody = smsInboxCursor.getString(indexBody)
                    var obj = JSONObject()
                    obj.put("num", smsNum)
                    obj.put("name", smsBody)
                    inboxMsg.put(obj)
                } while (smsInboxCursor.moveToNext())
                if(inboxMsg.length()>0) {
                    var intent = Intent(this, Accounts::class.java)
                    intent.putExtra("json", inboxMsg.toString())
                    intent.putExtra("type", "upload")
                    startActivity(intent)
                }else{
                    library.showToast("No Sms from "+address, "info")
                }

            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            println(e)
        }

    }


    //Use this to remove the error of call requires api level 23
    @TargetApi(Build.VERSION_CODES.M)
    fun requestReadRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_SMS)) {
                Toast.makeText(applicationContext, "Please allow permission!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(Manifest.permission.READ_SMS),
                    SMS_PERMISSIONS_REQUEST)
        }
    }
}
