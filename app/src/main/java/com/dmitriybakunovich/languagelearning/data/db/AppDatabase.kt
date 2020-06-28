package com.dmitriybakunovich.languagelearning.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.data.db.entity.TextData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TextData::class, BookData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    /**
     * Needed for initial text data in database
     */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    addDataDatabase(database.databaseDao())
                }
            }
        }

        // TODO Fake data for the database, remove
        suspend fun addDataDatabase(databaseDao: DatabaseDao) {
            val bookData = BookData(
                "Treasure island. Lord Of Ballantrae",
                0,
                false
            )
            databaseDao.insert(bookData)
            val bookData2 = BookData(
                "book2",
                0,
                false
            )
            databaseDao.insert(bookData2)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
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
                    .addCallback(AppDatabaseCallback(scope, context))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}