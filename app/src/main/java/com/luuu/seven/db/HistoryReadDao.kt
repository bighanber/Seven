package com.luuu.seven.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luuu.seven.bean.ReadHistoryBean

@Dao
interface HistoryReadDao {

    @Insert
    suspend fun insertHistory(readHistoryBean: ReadHistoryBean)

    @Update
    suspend fun updateReadHistory(readHistoryBean: ReadHistoryBean)

    @Query("select exists(select * from history_read where comic_id = :comicId)")
    suspend fun isReadInChapter(comicId: Int): Boolean

    @Query("select * from history_read where comic_id = :comicId")
    suspend fun queryByComicId(comicId: Int): List<ReadHistoryBean>

    @Query("select * from history_read")
    suspend fun getReadHistory(): List<ReadHistoryBean>
}