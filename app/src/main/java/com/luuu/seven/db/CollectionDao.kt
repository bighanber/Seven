package com.luuu.seven.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luuu.seven.bean.CollectBean
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert
    suspend fun insertCollection(collectBean: CollectBean)

    @Query("select exists(select * from comic_collect where comic_id = :comicId)")
    fun getCollectionById(comicId: Int): Flow<Boolean>

    @Query("delete from comic_collect where comic_id = :comicId")
    suspend fun cancelCollection(comicId: Int)

    @Query("select * from comic_collect")
    fun getCollectionData(): Flow<List<CollectBean>>
}