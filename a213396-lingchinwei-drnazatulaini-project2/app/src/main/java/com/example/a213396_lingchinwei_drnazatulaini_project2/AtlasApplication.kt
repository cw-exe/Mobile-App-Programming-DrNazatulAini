package com.example.a213396_lingchinwei_drnazatulaini_project2

import android.app.Application
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.AppContainer
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.AppDataContainer

class AtlasApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
