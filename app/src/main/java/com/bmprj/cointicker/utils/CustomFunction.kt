package com.bmprj.cointicker.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bmprj.cointicker.R
import java.text.SimpleDateFormat
import java.util.Date


@SuppressLint("SimpleDateFormat")
fun String.setDateTime(context: Context): String{

    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dat: Date = inFormat.parse(this) as Date
    val outFormatDays = SimpleDateFormat("yyyy")
    val goal: String = outFormatDays.format(dat)
    val outFormatMonth = SimpleDateFormat("MM")
    val month:String = outFormatMonth.format(dat)
    val outFormatDay = SimpleDateFormat("dd")
    val dy : String = outFormatDay.format(dat)

    val outFormatHour = SimpleDateFormat("HH")
    val hour: String = outFormatHour.format(dat)
    val outFormatMinute = SimpleDateFormat("mm")
    val minute: String = outFormatMinute.format(dat)
    val outFormatSecond = SimpleDateFormat("ss")
    val second: String = outFormatSecond.format(dat)

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


