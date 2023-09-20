package com.bmprj.cointicker.utils

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel


fun Fragment.logError(message:String?)= logError(this, message)

fun Activity.logError(message: String?) = logError(this,message)

fun ViewModel.logError(message: String?) = logError(this,message)

@JvmName("logFragment")
private fun logError(fragment:Fragment, message:String?){
    val className = fragment.javaClass.simpleName // or fragment::class.simpleName  -> //find class name
   // val methodName = fragment.get _???????????? todo can't get method name && do this is possible in adapter ?
    message?.let {
        Log.e(className, message)
    }
}


@JvmName("logActivity")
private fun logError(activity: Activity, message: String?){
    val className = activity::class.simpleName //get Activity class name
    message?.let {
        Log.e(className,message)
    }
}

@JvmName("logViewModel")
private fun logError(viewModel: ViewModel,message: String?){
    message?.let {
        Log.e(viewModel::class.simpleName,message)
    }
}
