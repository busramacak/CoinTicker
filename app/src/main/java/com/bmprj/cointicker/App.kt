package com.bmprj.cointicker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() // TODO uygulama crash bir alert göstermek isityorum

// multidext app -> bir kaç sene öncesine kadar proje dosyalarının (.dex) içindeki metodlar
// toplamının 64k yı aşamıyordu. hatta projelerden metod silmeleri gerekebiliyormuş.
// bu da ondan sonra çözüm amaçlı çıkarılmış. birden fazla dex kullanılmasını sağlıyor. android 5 den
// sonra destek gelmiş ama öncesi için kullanılması gereken kütüphane.