package com.bmprj.cointicker

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bmprj.cointicker.ui.MainActivity
import com.bmprj.cointicker.ui.login.LoginFragment
import com.bmprj.cointicker.utils.customAlert
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application(){

    override fun onCreate() {
        super.onCreate()

        //araştırdım
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleException(thread,throwable)
        }
    }


    private fun handleException(thread: Thread, throwable: Throwable){
        Log.e(applicationContext.javaClass.simpleName,throwable.message.toString())
//        customAlert("error",applicationContext,throwable.message.toString())
    }
} // TODO uygulama crash bir alert göstermek isityorum
// todo contexti alamadım uygulamanın aktif olduğuna emin misin hatası dönüyor try catch ile bakınca

// multidext app -> bir kaç sene öncesine kadar proje dosyalarının (.dex) içindeki metodlar
// toplamının 64k yı aşamıyordu. hatta projelerden metod silmeleri gerekebiliyormuş.
// bu da ondan sonra çözüm amaçlı çıkarılmış. birden fazla dex kullanılmasını sağlıyor. android 5 den
// sonra destek gelmiş ama öncesi için kullanılması gereken kütüphane.