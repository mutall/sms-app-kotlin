package com.hack3r.amshel.mutall.utilities

import android.telephony.SmsManager
import org.json.JSONArray
import org.json.JSONException

class SendSms(val array: JSONArray):Thread(), Runnable{

    override fun run() {
        sendMessages()
    }

    fun sendMessages(){
        val manager = SmsManager.getDefault()

        var msgLen = 0

        while (msgLen <= array.length()){
            try {
                var json = array.getJSONObject(msgLen)
                var body = json.getString("num")

                var phone = "95551"
                manager.sendTextMessage(phone, null, body, null, null)
                sleep(5000)
            }catch (e:JSONException){
                e.printStackTrace()
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
            msgLen ++
        }
    }
}