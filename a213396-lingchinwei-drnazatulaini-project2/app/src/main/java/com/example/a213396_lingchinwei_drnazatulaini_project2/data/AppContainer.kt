package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import android.content.Context

interface AppContainer {
    val helpRequestsRepository: HelpRequestsRepository
    val communityHelpRepository: CommunityHelpRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val helpRequestsRepository: HelpRequestsRepository by lazy {
        OfflineHelpRequestsRepository(AtlasDatabase.getDatabase(context).helpRequestDao())
    }

    override val communityHelpRepository: CommunityHelpRepository by lazy {
        CommunityHelpRepository()
    }
}