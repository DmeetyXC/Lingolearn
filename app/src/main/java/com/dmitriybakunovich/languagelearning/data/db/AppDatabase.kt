package com.dmitriybakunovich.languagelearning.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dmitriybakunovich.languagelearning.R
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

        // Fake data for the database
        suspend fun addDataDatabase(databaseDao: DatabaseDao) {
            val bookData = BookData(
                "book1",
                0
            )
            databaseDao.insert(bookData)
            val textData = TextData(
                "book1",
                context.getString(R.string.text_book_simple),
                context.getString(R.string.text_book_simple_translate)
            )
            databaseDao.insert(textData)
            val textData2 = TextData(
                "book1",
                "Какой-то текст для второй страницы",
                "Some text for the second page"
            )
            databaseDao.insert(textData2)
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