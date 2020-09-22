package com.luuu.seven.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luuu.seven.bean.ReadHistoryBean
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryReadDao {

    @Insert
    suspend fun insertHistory(readHistoryBean: ReadHistoryBean)

    @Update
    suspend fun updateReadHistory(readHistoryBean: ReadHistoryBean)

    @Query("select exists(select * from history_read where comic_id = :comicId)")
    fun isReadInChapter(comicId: Int): Flow<Boolean>

    @Query("select * from history_read where comic_id = :comicId")
    fun queryByComicId(comicId: Int): Flow<List<ReadHistoryBean>>

    @Query("select * from history_read")
    fun getReadHistory(): Flow<List<ReadHistoryBean>>
}