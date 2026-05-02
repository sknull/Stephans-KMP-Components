package de.visualdigits.common.presentation.components

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import de.visualdigits.common.domain.model.platform.ConnectivityMode

actual class ConnectivityManager(
    private val context: Context
) {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    actual fun connectivityMode(): ConnectivityMode {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectivityMode.wifi
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectivityMode.cellular
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectivityMode.ethernet
                    else -> ConnectivityMode.disconnected
                }
            } ?: ConnectivityMode.disconnected
        } ?: ConnectivityMode.disconnected
    }
}
