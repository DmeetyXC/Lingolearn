package com.dmeetyxc.lingolearn.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.data.entity.TextData

@Database(
    entities = [TextData::class, BookData::class, Dictionary::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}