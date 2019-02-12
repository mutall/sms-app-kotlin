package com.hack3r.amshel.mutall

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog
import com.hack3r.amshel.mutall.utilities.ACCOUNTS_URL
import com.hack3r.amshel.mutall.utilities.Library
import com.hack3r.amshel.mutall.utilities.NOREADING_URL
import com.hack3r.amshel.mutall.utilities.VolleyController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    lateinit var urls: Map<String, String>

    lateinit var messageType: Array<String>

    lateinit var library : Library
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        library = Library(this)
        messageType = resources.getStringArray(R.array.accounts)
        urls = mapOf(messageType[0] to ACCOUNTS_URL, messageType[1] to NOREADING_URL)

        retrieveAccounts.setOnClickListener {
            val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.account_type)
            alertDialog.setItems(messageType, DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int -> retrieveAccounts(urls[messageType[which]]) })
            alertDialog.create().show()
        }


    }

    fun retrieveAccounts(url:String?){
        library.showProgress("fetching")
        val jsonArrayRequest  = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener {response ->
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
                                library.showToast("success", "success")
                            }

                            .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setNegativeButtonText("SEND TO KPLC")
                            .setNegativeButtonClick {
                                library.showToast("success", "info")
                            }
                            .show()
                },
                Response.ErrorListener {error: VolleyError ->
                    library.dismissProgress()
                    Log.e(TAG, error.message) })

        VolleyController.instance.addRequestQueue(jsonArrayRequest)
    }
}
