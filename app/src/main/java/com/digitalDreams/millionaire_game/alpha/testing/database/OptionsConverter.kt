package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class OptionsConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromOptions(options: Question.Options):String{
        return gson.toJson(options)
    }

    @TypeConverter
    fun toOptions(optionString: String): Question.Options {
        return gson.fromJson(optionString, Question.Options::class.java)
    }
}