package com.uqudo.android.sample

import android.app.Application
import io.uqudo.sdk.core.UqudoSDK

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UqudoSDK.init(this)
    }
}