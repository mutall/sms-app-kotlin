package com.hack3r.amshel.mutall.controllers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.hack3r.amshel.mutall.R
import com.hack3r.amshel.mutall.models.MessageObject
import com.hack3r.amshel.mutall.utilities.*
import kotlinx.android.synthetic.main.activity_recycler.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Accounts:AppCompatActivity() {
    lateinit var adapter:SmsAdapter
    lateinit var type:String
    lateinit var accounts_array:JSONArray
    lateinit var library:Library;
    lateinit var key1:String
    lateinit var key2:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val accounts = intent.getStringExtra("json");
        type = intent.getStringExtra("type")
        key1 = intent.getStringExtra("key1")
        key2 = intent.getStringExtra("key2")
        library = Library(this);
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

                var msgObj = MessageObject(obj.getString(key1), obj.getString(key2))
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
                Response.Listener { response ->
                    Log.i(DEBUG, response)
                    val intent = Intent(this, WebView::class.java)
                    intent.putExtra(RESPONSE, response)
                    startActivity(intent)
                    },
                Response.ErrorListener { error -> Log.e(DEBUG, error.message)  }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                print(accounts_array);
                params["msg"] = accounts_array.toString()
                return params

            }
        }
        VolleyController.instance.addRequestQueue(stringRequest)
    }


    fun smsSend(){
        val activity:Thread = SendSms(accounts_array)
        activity.start()
    }


//    {
//        @Throws(AuthFailureError::class)
//        protected override fun getParams(): Map<String, String> {
//
//            val `object` = JSONObject()
//            try {
//                `object`.put("type", "all")
//            } catch (e: JSONException) {
//                library.showSnack(e.message)
//            }
//
//            val jsonArray = JSONArray()
//            jsonArray.put(`object`)
//            val params = java.util.HashMap<String, String>()
//            params["json"] = `object`.toString()
//            return params
//        }
}