package com.hack3r.amshel.mutall.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast

import com.awesomedialog.blennersilva.awesomedialoglibrary.*
import com.hack3r.amshel.mutall.R
import com.hack3r.amshel.mutall.models.ErrorObject

import java.util.HashMap

import es.dmoral.toasty.Toasty


// TODO: 24/10/2018 set different icons for dialogs
class Library//constructor
(internal var context: Activity) {
     var errors: MutableList<ErrorObject>
     var progressDialog: AwesomeProgressDialog = AwesomeProgressDialog(context)
     var integerStringHashMap: HashMap<String, String>

    init {
        errors = ArrayList<ErrorObject>()
        integerStringHashMap = HashMap()
    }

    /**
     * Simplae method for displaying a tost notification box in the current activity
     * In future it will be replaced by a snackbar because it is more customizible
     * @param text
     * use the toasty library
     */
    fun showToast(text: String, type: String) {
        when (type) {
            "error" -> Toasty.error(context, text, Toast.LENGTH_LONG).show()
            "success" -> Toasty.success(context, text, Toast.LENGTH_LONG).show()
            "info" -> Toasty.info(context, text, Toast.LENGTH_LONG).show()
            "warning" -> Toasty.warning(context, text, Toast.LENGTH_LONG).show()
            else -> Toasty.normal(context, text, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * write a function to display a snackbar
     * snackbar is better to use than a toast because the user has more frredom and flexibility
     * Pass the message you want to be displayed by the snackbar
     * Remember the snackbar requires the android.support.design library
     * So import that library before proceeding.
     */
    fun showSnack(message: String) {
        val view = context.findViewById<View>(android.R.id.content).rootView
        val snackbar: Snackbar
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("close") { snackbar.dismiss() }
        snackbar.show()
    }

    /**
     * Do a progress dialog for network requests and also for situations where a thread takes time
     * Also involve a function for cancelling the progress dialog
     */
    fun showProgress(message: String) {
        progressDialog = AwesomeProgressDialog(context)
        progressDialog.setMessage(message)
                .setTitle("WORKING")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .show()
    }

    fun dismissProgress() {
        progressDialog.hide()
    }

    /**
     *
     * @param context in what current context.
     * @param type the type of dialog. either error message etc
     * @param message The message to be displayed to the user
     */
    fun showDialog(context: Context, type: String, title: String, message: String) {
        when (type) {
            "error" -> {
                val dialog = AwesomeErrorDialog(context)
                dialog.setTitle(title)
                dialog.setMessage(message)
                dialog.setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                dialog.setColoredCircle(R.color.md_red_600)
                dialog.setCancelable(true).setButtonText(context.resources.getString(R.string.dialog_ok_button))
                dialog.setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                dialog.setButtonText("okay")
                dialog.setErrorButtonClick { }
                dialog.show()
            }

            "success" -> AwesomeSuccessDialog(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText(context.resources.getString(R.string.dialog_ok_button))
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick {
                        //click
                    }.show()

            "warning" -> AwesomeWarningDialog(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setColoredCircle(R.color.md_orange_600)
                    .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                    .setCancelable(true)
                    .setButtonBackgroundColor(R.color.md_orange_600)
                    .setButtonText(context.resources.getString(R.string.dialog_ok_button))
                    .setWarningButtonClick {
                        // click
                    }
                    .show()

            "info" -> AwesomeInfoDialog(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setColoredCircle(R.color.md_light_blue_600)
                    .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonbackgroundColor(R.color.md_light_blue_600)
                    .setPositiveButtonText(context.resources.getString(R.string.dialog_ok_button))
                    .setPositiveButtonClick {
                        // click
                    }
                    .show()
        }
    }

    /**
     * Build string info given a hash maps of strings
     * @param messages
     * @return
     */
    fun messageBuilder(messages: HashMap<String, String>): String {
        val builder = StringBuilder()
        for (msg in messages.keys) {
            val value = messages[msg]
            builder.append("$msg: $value\n")
        }
        return builder.toString()
    }

    /**
     * Check whether the the device has an internet connection
     * why?? This is because i want to  upload the reading directly;
     * @return
     */
    fun hasInternetConnectivity(): Boolean {
        val hasInternet: Boolean

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        hasInternet = activeNetwork != null && activeNetwork.isConnected

        return hasInternet
    }

    fun addError(error: ErrorObject) {
        //we cant iterate and add at the same time
        val toAdd = ArrayList<ErrorObject>()
        if (this.errors.size > 0) {
            for (e in this.errors) {
                if (e.code !== error.code) {
                    toAdd.add(error)
                }
            }
            for (e in toAdd) {
                this.errors.add(e)
            }
        } else {
            this.errors.add(error)
        }

    }

    /**
     * add list of errors
     * @param errorList
     */
    fun addErrorList(errorList: List<ErrorObject>) {
        for (errorObject in errorList) {
            addError(errorObject)
        }
    }

    /**
     * Remove an error from list if given the error codes
     * @param code
     */
    fun removeError(code: Int) {
        val toRemove = arrayListOf<ErrorObject>()
        for (e in this.errors) {
            if (e.code === code) {
                toRemove.add(e)
            }
        }
        this.errors.removeAll(toRemove)
    }

    fun convertListToHashMap(): HashMap<String, String> {
        if (this.errors.size > 0) {
            for (e in this.errors) {
                integerStringHashMap[e.code.toString()] = e.message;
            }
        }
        return integerStringHashMap
    }
}
