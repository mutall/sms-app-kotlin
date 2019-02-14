package com.hack3r.amshel.mutall.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.hack3r.amshel.mutall.R
import com.hack3r.amshel.mutall.models.MessageObject
import com.hack3r.amshel.mutall.utilities.POST_BILLS
import com.hack3r.amshel.mutall.utilities.SendSms
import com.hack3r.amshel.mutall.utilities.SmsAdapter
import com.hack3r.amshel.mutall.utilities.VolleyController
import kotlinx.android.synthetic.main.activity_recycler.*
import org.json.JSONArray
import org.json.JSONException

class Accounts:AppCompatActivity() {
    lateinit var adapter:SmsAdapter
    lateinit var type:String
    lateinit var accounts_array:JSONArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val accounts = intent.getStringExtra("json");
        type = intent.getStringExtra("type")

        btn.text = type
        btn.setOnClickListener {
            when(type){
                "upload" ->uploadBills()
                "send"-> smsSend()
            }
        }

        var list: ArrayList<MessageObject> = ArrayList()

        accounts_array = JSONArray(accounts)

        var x = 0
        while (x <= accounts_array.length()){
            try {
                var obj = accounts_array.getJSONObject(x)

                var msgObj = MessageObject(obj.getString("num"), obj.getString("name"))
                list.add(msgObj)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            x++
        }
        adapter = SmsAdapter(this, list)
        listview.adapter = adapter
    }

    fun uploadBills() {
        var stringRequest = object : StringRequest(Request.Method.POST, POST_BILLS,
                Response.Listener { },
                Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("msg", accounts_array.toString())
                return params
            }
        }
        VolleyController.instance.addRequestQueue(stringRequest)
    }


    fun smsSend(){
        val activity:Thread = SendSms(accounts_array)
        activity.start()
    }
}