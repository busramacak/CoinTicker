package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName


data class Low24h(
    @SerializedName("usd")
    val usd: Double
)



//value class yaptığımda @JVMInline etiketini istedi. coinDetailFragment
// açıldığında (bu veriyi orada kullanıyorum) sadece bunu değil bütün o ekrandaki verileri de getirmedi.