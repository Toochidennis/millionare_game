package com.digitalDreams.millionaire_game.alpha.testing.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: List<Question>)

    @Query("SELECT * FROM questions WHERE language = :language AND level = :level ORDER BY RANDOM() LIMIT 1")
    suspend fun getQuestionByLanguageAndLevel(language: String, level: String): Question?

    @Query("SELECT COUNT() FROM questions")
    fun getQuestionSize(): Int
}