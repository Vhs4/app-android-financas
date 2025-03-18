package com.example.fin_edu_app.model

import model.Choice

data class Question(
    val id: Int,
    val text: String,
    val choices: List<Choice>
)

