package com.digitalDreams.millionaire_game.alpha.models

data class QuestionModel(
    val questionId: String = "",
    val questionText: String = "",
    val correctText: String = "",
    val reasonText: String = "",
    val options: List<OptionsModel> = arrayListOf()
)
