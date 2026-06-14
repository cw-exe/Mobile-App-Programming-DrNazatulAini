package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import kotlinx.coroutines.flow.Flow

class OfflineHelpRequestsRepository(private val helpRequestDao: HelpRequestDao) : HelpRequestsRepository {
    override fun getAllHelpRequestsStream(): Flow<List<HelpRequest>> = helpRequestDao.getAllHelpRequests()
    override suspend fun insertHelpRequest(request: HelpRequest) = helpRequestDao.insert(request)
    override suspend fun deleteHelpRequest(request: HelpRequest) = helpRequestDao.delete(request)
}
