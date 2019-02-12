package com.hack3r.amshel.mutall.helpers

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyController : Application() {
    /**
     * we need three methods for handling the requests
     * 1:getRequestQueue which replaces the Volley.newRequestQueue
     * 2:addRequestQueue which adds a request to a queue
     * 2:cancelRequestQueue which cancels a request
     */

    var requestQueue: RequestQueue? = null
        private set
    private val TAG = VolleyController::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        ourInstance = this
        requestQueue = Volley.newRequestQueue(applicationContext)
    }

    fun <T> addRequestQueue(rq: Request<T>) {
        rq.tag = requestQueue
        requestQueue?.add<T>(rq)
    }

    fun cancelRequestQueue() {
        requestQueue!!.cancelAll(TAG)
    }

    companion object {
        private var ourInstance: VolleyController? = null

        val instance: VolleyController
            get() {
                return ourInstance ?: VolleyController()
            }
    }

}
