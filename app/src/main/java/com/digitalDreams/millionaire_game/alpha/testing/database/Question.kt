package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    data class Options(
        val a: String,
        val b: String,
        val c: String,
        val d: String,
    )
}
