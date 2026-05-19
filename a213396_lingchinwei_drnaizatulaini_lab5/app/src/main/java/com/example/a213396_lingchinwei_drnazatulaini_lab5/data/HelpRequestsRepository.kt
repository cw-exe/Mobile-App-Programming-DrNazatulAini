package com.example.a213396_lingchinwei_drnazatulaini_lab5.data

import kotlinx.coroutines.flow.Flow

interface HelpRequestsRepository {
    fun getAllHelpRequestsStream(): Flow<List<HelpRequest>>
    suspend fun insertHelpRequest(request: HelpRequest)
    suspend fun deleteHelpRequest(request: HelpRequest)
}
