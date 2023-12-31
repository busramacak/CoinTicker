package com.bmprj.cointicker.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkManager(
    val context: Context,
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    fun checkNetworkAvailable(): Boolean =
        try {
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else false
        } catch (e: Exception) {
            false
        }

    fun isVpnActive() = capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

    @RequiresApi(Build.VERSION_CODES.N)
    fun setNetworkStateListener(available: () -> Unit, unAvailable: () -> Unit) {

        connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    available()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    unAvailable()
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    unAvailable()
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    unAvailable()
                }
            })
    }
}