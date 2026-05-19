package com.example.a213396_lingchinwei_drnazatulaini_lab5.data

import android.content.Context

interface AppContainer {
    val helpRequestsRepository: HelpRequestsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val helpRequestsRepository: HelpRequestsRepository by lazy {
        OfflineHelpRequestsRepository(AtlasDatabase.getDatabase(context).helpRequestDao())
    }
}
