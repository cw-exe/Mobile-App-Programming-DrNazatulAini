package com.example.a213396_lingchinwei_drnazatulaini_lab5.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HelpRequestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(request: HelpRequest)

    @Delete
    suspend fun delete(request: HelpRequest)

    @Query("SELECT * FROM help_requests ORDER BY timestamp DESC")
    fun getAllHelpRequests(): Flow<List<HelpRequest>>
}
