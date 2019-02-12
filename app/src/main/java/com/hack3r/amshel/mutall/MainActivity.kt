package com.hack3r.amshel.mutall

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hack3r.amshel.mutall.helpers.Library
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val library = Library(this);

        retrieveAccounts.setOnClickListener {

        }
    }
}
