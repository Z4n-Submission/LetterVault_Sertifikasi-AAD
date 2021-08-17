package com.google.developers.lettervault.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Letter::class], version = 1, exportSchema = false)
abstract class LetterDatabase : RoomDatabase() {

    abstract fun letterDao(): LetterDao

    companion object {

        @Volatile
        private var instance: LetterDatabase? = null

        /**
         * Returns an instance of Room Database.
         *
         * @param context application context
         * @return The singleton LetterDatabase
         */
        fun getInstance(context: Context): LetterDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, LetterDatabase::class.java, "letter.db")
                    .build()
            }
        }
    }
}
