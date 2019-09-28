package com.luuu.seven.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luuu.seven.ComicApplication
import com.luuu.seven.bean.ReadHistoryBean

@Database(entities = [ReadHistoryBean::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryReadDao

    companion object {
        private const val databaseName = "comic-db"

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(ComicApplication.mApp).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration().build()
        }
    }
}