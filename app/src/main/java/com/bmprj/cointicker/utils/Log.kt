package com.bmprj.cointicker.utils

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel


fun Fragment.logError(message:String?)= logError(this, message)

fun Activity.logError(message: String?) = logError(this,message)

fun ViewModel.logError(message: String?) = logError(this,message)

fun Any.logError(message: String?)=logError(this,message)

@JvmName("logFragment")
private fun logError(fragment:Fragment, message:String?){
    val className = fragment::class.java.simpleName
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

@JvmName("logAny")
private fun logError(any: Any,message: String?){
    message?.let {
        Log.e(any::class.java.simpleName,message)
    }
}
