package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [Question::class], version = 1)
@TypeConverters(OptionsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}