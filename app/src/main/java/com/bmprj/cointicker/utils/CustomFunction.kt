package com.bmprj.cointicker.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.bmprj.cointicker.R
import java.text.SimpleDateFormat
import java.util.Date




//todo olmadı
@RequiresApi(Build.VERSION_CODES.S)
fun Fragment.navigate(findNavController: NavController,transition:NavDirections){
    val currentPackage = this::class.java.packageName
    val directionClass = transition::class.java.packageName
    if(currentPackage==directionClass){
        findNavController.navigate(transition)
    }
}

@SuppressLint("SimpleDateFormat")
fun String.setDateTime(context: Context): String{

    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date: Date = inFormat.parse(this) as Date
    val outFormatDays = SimpleDateFormat("yyyy")
    val goal: String = outFormatDays.format(date)
    val outFormatMonth = SimpleDateFormat("MM")
    val month:String = outFormatMonth.format(date)
    val outFormatDay = SimpleDateFormat("dd")
    val dy : String = outFormatDay.format(date)

    val outFormatHour = SimpleDateFormat("HH")
    val hour: String = outFormatHour.format(date)
    val outFormatMinute = SimpleDateFormat("mm")
    val minute: String = outFormatMinute.format(date)
    val outFormatSecond = SimpleDateFormat("ss")
    val second: String = outFormatSecond.format(date)

    val lastDate = context.getString(R.string.lastupdate,goal,month,dy,hour,minute,second)

    return lastDate

}

fun String.fixedString():String{
    val hrefPattern = """<a href="([^"]*)">([^<]*)</a>""".toRegex()
    val matcher = hrefPattern.findAll(this)
    val duzeltilmisVeri = StringBuilder()
    var sonIndex = 0

    matcher.forEach { match ->
        duzeltilmisVeri.append(this.substring(sonIndex, match.range.first)) // Önceki metin
        val metin = match.groupValues[2] // Bağlantı metni
        duzeltilmisVeri.append(metin) // Düzeltilmiş metni ekle
        sonIndex = match.range.last + 1
    }
    duzeltilmisVeri.append(this.substring(sonIndex)) // Geri kalan metin

    return duzeltilmisVeri.toString()
}

fun View.setUpDialog(context: Context):AlertDialog{
    val dialog = AlertDialog.Builder(context)
        .setView(this)
        .setCancelable(true)
        .create()

    val window: Window? = dialog.window
    val wlp = window?.attributes

    wlp?.gravity = Gravity.BOTTOM
    wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
    window?.attributes = wlp

    return dialog
}


fun customAlert(title:String, context:Context, msg:String){
  AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("Ok") { dialogInterface, _ ->
            Log.e("nolllll,", "hata")
            dialogInterface.dismiss()
        }.create().show()
}


