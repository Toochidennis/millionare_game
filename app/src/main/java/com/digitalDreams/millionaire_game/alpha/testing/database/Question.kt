package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "questions")
data class Question(
    @ColumnInfo(name = "question_id") @PrimaryKey var questionId: String,
    var question: String,
    @ColumnInfo(name = "correct_answer") var correctAnswer: String,
    var options: Options,
    var reason: String,
    @ColumnInfo(name = "stage_name") var stageName: String,
    var stage: String,
    var level: String,
    var language: String,
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("questionId", questionId)
            put("question", question)
            put("correctAnswer", correctAnswer)
            put("options", options.toJsonObject())
            put("reason", reason)
            put("stageName", stageName)
            put("stage", stage)
            put("level", level)
            put("language", language)
        }
    }

    companion object {
        fun fromJsonObject(jsonString: String): Question {
            val jsonObject = JSONObject(jsonString)
            val questionId = jsonObject.getString("questionId")
            val question = jsonObject.getString("question")
            val correctAnswer = jsonObject.getString("correctAnswer")
            val optionsJson = jsonObject.getString("options")
            val options = Options.fromJsonObject(optionsJson) // Deserialize nested object
            val reason = jsonObject.getString("reason")
            val stageName = jsonObject.getString("stageName")
            val stage = jsonObject.getString("stage")
            val level = jsonObject.getString("level")
            val language = jsonObject.getString("language")

            return Question(
                questionId,
                question,
                correctAnswer,
                options,
                reason,
                stageName,
                stage,
                level,
                language
            )
        }
    }


    data class Options(
        val a: String,
        val b: String,
        val c: String,
        val d: String,
    ) {
        fun toJsonObject(): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put("a", a)
            jsonObject.put("b", b)
            jsonObject.put("c", c)
            jsonObject.put("d", d)
            return jsonObject
        }

        companion object {
            fun fromJsonObject(jsonString: String): Options {
                val jsonObject = JSONObject(jsonString)
                val a = jsonObject.getString("a")
                val b = jsonObject.getString("b")
                val c = jsonObject.getString("c")
                val d = jsonObject.getString("d")
                return Options(a, b, c, d)
            }
        }
    }
}
