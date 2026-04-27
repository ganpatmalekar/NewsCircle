package com.gsm.newscircle.utils

class TestNetworkHelper: NetworkHelper {
    override fun isNetworkConnected(): Boolean {
        return true
    }
}