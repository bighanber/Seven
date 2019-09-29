package com.luuu.seven.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luuu.seven.bean.CollectBean

@Dao
interface CollectionDao {

    @Insert
    suspend fun insertCollection(collectBean: CollectBean)

    @Query("select * from comic_collect where comic_id = :comicId")
    suspend fun getCollectionById(comicId: Int): Boolean

    @Query("delete from comic_collect where comic_id = :comicId")
    suspend fun cancelCollection(comicId: Int)

    @Query("select * from comic_collect")
    suspend fun getCollectionData(): List<CollectBean>
}