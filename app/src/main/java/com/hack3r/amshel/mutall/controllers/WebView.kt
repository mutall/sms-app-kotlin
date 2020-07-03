package com.hack3r.amshel.mutall.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import com.hack3r.amshel.mutall.R
import com.hack3r.amshel.mutall.utilities.RESPONSE
import kotlinx.android.synthetic.main.activity_webview.*


class WebView:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        if(intent.hasExtra(RESPONSE)) {
            val response = intent.getStringExtra(RESPONSE)
            val encodedHtml = Base64.encodeToString(response.toByteArray(), Base64.NO_PADDING)

            webview.loadData(encodedHtml, "text/html", "base64")
        }
    }
}