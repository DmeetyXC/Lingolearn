package com.dmeetyxc.lingolearn.data.db

import androidx.room.Database
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
}