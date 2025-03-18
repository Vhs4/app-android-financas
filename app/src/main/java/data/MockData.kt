package com.example.fin_edu_app.data

import com.example.fin_edu_app.model.Question
import model.Choice

fun generateMockQuestions(): List<Question> {
    return listOf(
        Question(
            id = 1,
            text = "O que é um investimento?",
            choices = listOf(
                Choice("Aplicar dinheiro para obter retorno", 10.0),
                Choice("Guardar dinheiro em casa", -5.0),
                Choice("Gastar dinheiro em compras", -10.0)
            )
        ),
        Question(
            id = 2,
            text = "Qual é a melhor forma de economizar dinheiro?",
            choices = listOf(
                Choice("Fazer um orçamento mensal", 10.0),
                Choice("Gastar sem controle", -10.0),
                Choice("Pedir dinheiro emprestado", -5.0)
            )
        )
        // Adicione mais perguntas conforme necessário
    )
}