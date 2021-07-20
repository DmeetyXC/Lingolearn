package com.dmeetyxc.lingolearn.data.repository

import com.dmeetyxc.lingolearn.data.db.DatabaseDao
import javax.inject.Inject

class FavoriteBooksRepository @Inject constructor(private val database: DatabaseDao) {
    fun getFavoriteBook() = database.getFavoriteBook()
}