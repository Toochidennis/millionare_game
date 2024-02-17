package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.TypeConverter
import org.json.JSONObject

class OptionsConverter {

    @TypeConverter
    fun fromOptions(options: Question.Options): String {
        val jsonObject = JSONObject()
        jsonObject.put("a", options.a)
        jsonObject.put("b", options.b)
        jsonObject.put("c", options.c)
        jsonObject.put("d", options.d)
        return jsonObject.toString()
    }

    @TypeConverter
    fun toOptions(jsonString: String): Question.Options {
        val jsonObject = JSONObject(jsonString)
        return Question.Options(
            jsonObject.getString("a"),
            jsonObject.getString("b"),
            jsonObject.getString("c"),
            jsonObject.getString("d")
        )
    }

}