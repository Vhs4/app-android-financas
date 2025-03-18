package com.example.fin_edu_app.ui.theme.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int = 0 // Index of correct answer, default to first option
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FinancialQuizScreen(navController: NavController) {
    val questions = listOf(
        QuizQuestion(
            "Para atingir uma estabilidade financeira, quando você tem que ter economizado?",
            listOf(
                "Um mês do custo fixo do mês",
                "Dois mêses do custo fixo do mês",
                "Três mêses do custo fixo do mês",
                "Quatro mêses do custo fixo do mês",
                "Um ano do custo fixo do mês"
            )
        ),
        QuizQuestion(
            "Qual das opções é fundamental para evitar o endividamento excessivo?",
            listOf(
                "Planejar os gastos mensais",
                "Comprar por impulso",
                "Utilizar cartões de crédito sem controle",
                "Ignorar as despesas variáveis",
                "Adiar o pagamento de contas"
            )
        ),
        QuizQuestion(
            "Qual prática contribui para a formação de um fundo de emergência?",
            listOf(
                "Poupar uma porcentagem do salário regularmente",
                "Gastar todo o salário em necessidades imediatas",
                "Recorrer a empréstimos para emergências",
                "Não controlar os gastos mensais",
                "Utilizar o crédito para imprevistos"
            )
        ),
        QuizQuestion(
            "Qual é a vantagem de registrar todas as despesas diárias?",
            listOf(
                "Permite identificar padrões de gastos e ajustar o orçamento",
                "Incentiva o consumo desnecessário",
                "Não tem impacto na saúde financeira",
                "Aumenta a chance de gastos impulsivos",
                "Dificulta a visualização do fluxo financeiro"
            )
        ),
        QuizQuestion(
            "Como a educação financeira pode melhorar sua qualidade de vida?",
            listOf(
                "Promovendo escolhas conscientes e o planejamento para o futuro",
                "Incentivando o consumo sem controle",
                "Aumentando o endividamento e os riscos financeiros",
                "Focando apenas em investimentos de alto risco",
                "Ignorando a importância do orçamento mensal"
            )
        )
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptions by remember { mutableStateOf(List(questions.size) { -1 }) }
    var quizCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        AnimatedContent(
            targetState = quizCompleted,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = "QuizCompletion"
        ) { isCompleted ->
            if (isCompleted) {
                // Quiz completion screen
                CompletionScreen(
                    score = score,
                    totalQuestions = questions.size,
                    onRestart = {
                        currentQuestionIndex = 0
                        selectedOptions = List(questions.size) { -1 }
                        quizCompleted = false
                        score = 0
                    },
                    navController = navController
                )
            } else {
                // Quiz question screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    // Progress indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressCircle(
                            progress = (currentQuestionIndex + 1).toFloat() / questions.size,
                            modifier = Modifier.size(160.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Quote icon
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFFBF1C))
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                QuoteIcon()
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Progress text
                            Text(
                                text = "${currentQuestionIndex + 1}/${questions.size}",
                                color = Color(0xFFFFBF1C),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Question
                    AnimatedContent(
                        targetState = currentQuestionIndex,
                        transitionSpec = {
                            slideInHorizontally { width -> width } togetherWith
                                    slideOutHorizontally { width -> -width }
                        },
                        label = "QuestionTransition"
                    ) { index ->
                        Text(
                            text = questions[index].question,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }

                    // Options
                    AnimatedContent(
                        targetState = currentQuestionIndex,
                        transitionSpec = {
                            slideInHorizontally { width -> width } togetherWith
                                    slideOutHorizontally { width -> -width }
                        },
                        label = "OptionsTransition"
                    ) { index ->
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            questions[index].options.forEachIndexed { optionIndex, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val newSelectedOptions = selectedOptions.toMutableList()
                                            newSelectedOptions[index] = optionIndex
                                            selectedOptions = newSelectedOptions
                                        }
                                        .padding(vertical = 12.dp)
                                ) {
                                    // Radio button
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .border(
                                                width = 2.dp,
                                                color = if (selectedOptions[index] == optionIndex) Color(
                                                    0xFFFFBF1C
                                                ) else Color.White,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selectedOptions[index] == optionIndex) {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .background(Color(0xFFFFBF1C), CircleShape)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // Option text
                                    Text(
                                        text = option,
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                    }

                    // Answer button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFBF1C))
                            .clickable {
                                if (selectedOptions[currentQuestionIndex] >= 0) {
                                    // Check if answer is correct
                                    if (selectedOptions[currentQuestionIndex] == questions[currentQuestionIndex].correctAnswer) {
                                        score++
                                    }

                                    // Move to next question or complete quiz
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                    } else {
                                        quizCompleted = true
                                    }
                                }
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Responder",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompletionScreen(
    score: Int,
    totalQuestions: Int,
    navController: NavController,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Trophy or completion icon
        Canvas(modifier = Modifier.size(120.dp)) {
            // Gold trophy
            drawCircle(
                color = Color(0xFFFFBF1C),
                radius = size.width / 2,
                center = Offset(size.width / 2, size.height / 2)
            )

            // Trophy cup shape
            drawRect(
                color = Color(0xFFFFD700),
                topLeft = Offset(size.width * 0.35f, size.height * 0.2f),
                size = Size(size.width * 0.3f, size.height * 0.4f)
            )

            // Trophy handles
            drawRect(
                color = Color(0xFFFFD700),
                topLeft = Offset(size.width * 0.25f, size.height * 0.3f),
                size = Size(size.width * 0.1f, size.height * 0.1f)
            )

            drawRect(
                color = Color(0xFFFFD700),
                topLeft = Offset(size.width * 0.65f, size.height * 0.3f),
                size = Size(size.width * 0.1f, size.height * 0.1f)
            )

            // Trophy base
            drawRect(
                color = Color(0xFFFFD700),
                topLeft = Offset(size.width * 0.4f, size.height * 0.6f),
                size = Size(size.width * 0.2f, size.height * 0.2f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Quiz Concluído!",
            color = Color(0xFFFFBF1C),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sua pontuação:",
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$score de $totalQuestions",
            color = Color(0xFFFFBF1C),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (score >= totalQuestions * 0.7) {
                "Parabéns! Você tem um bom conhecimento financeiro!"
            } else if (score >= totalQuestions * 0.4) {
                "Bom trabalho! Continue aprendendo sobre finanças."
            } else {
                "Continue estudando para melhorar seu conhecimento financeiro."
            },
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFBF1C),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Reiniciar Quiz",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { navController.navigate("goals") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFBF1C),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Ir para minhas metas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProgressCircle(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.1f
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.width - strokeWidth) / 2

        // Background circle
        drawArc(
            color = Color(0xFF4C4C4C),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(size.width - strokeWidth, size.height - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Progress arc
        drawArc(
            color = Color(0xFF9C9518),
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(size.width - strokeWidth, size.height - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun QuoteIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        // Chat bubble shape
        drawRoundRect(
            color = Color(0xFFFFBF1C),
            size = Size(size.width, size.height * 0.8f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.1f),
            style = Stroke(width = size.width * 0.1f)
        )

        // Dots (quote marks)
        val dotRadius = size.width * 0.1f
        val dotSpacing = size.width * 0.25f

        // Left dots
        drawCircle(
            color = Color.Black,
            radius = dotRadius,
            center = Offset(size.width * 0.35f, size.height * 0.4f)
        )

        drawCircle(
            color = Color.Black,
            radius = dotRadius,
            center = Offset(size.width * 0.35f, size.height * 0.55f)
        )

        // Right dots
        drawCircle(
            color = Color.Black,
            radius = dotRadius,
            center = Offset(size.width * 0.65f, size.height * 0.4f)
        )

        drawCircle(
            color = Color.Black,
            radius = dotRadius,
            center = Offset(size.width * 0.65f, size.height * 0.55f)
        )
    }
}