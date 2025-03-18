package com.example.fin_edu_app.ui.theme.pages


import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

data class FinancialGoal(
    val id: String,
    val name: String,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val period: String
)

class GoalsViewModel(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("financial_goals", Context.MODE_PRIVATE)

    private val _goals = MutableStateFlow<List<FinancialGoal>>(emptyList())
    val goals: StateFlow<List<FinancialGoal>> = _goals.asStateFlow()

    private val _showNewGoalDialog = MutableStateFlow(false)
    val showNewGoalDialog: StateFlow<Boolean> = _showNewGoalDialog.asStateFlow()

    private val _totalGoalsAchieved = MutableStateFlow(0)
    val totalGoalsAchieved: StateFlow<Int> = _totalGoalsAchieved.asStateFlow()

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

    private val _userBalance = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance.asStateFlow()

    init {
        loadGoals()
        loadUserStats()
    }

    private fun loadGoals() {
        val goalsJson = sharedPreferences.getString("goals", null)
        // In a real app, you would parse the JSON to a list of FinancialGoal objects
        // For simplicity, we'll just create some sample goals if none exist
        if (goalsJson == null) {
            _goals.value = listOf(
                FinancialGoal(
                    id = "1",
                    name = "Alimentação no dia-a-dia",
                    description = "Economizar em refeições fora de casa",
                    targetAmount = 300.0,
                    currentAmount = 75.0,
                    period = "Mês"
                )
            )
        }

        // Count achieved goals
        _totalGoalsAchieved.value = _goals.value.count { it.currentAmount >= it.targetAmount }
    }

    private fun loadUserStats() {
        _totalPoints.value = sharedPreferences.getInt("total_points", 3400)
        _userBalance.value = sharedPreferences.getFloat("user_balance", 0f).toDouble()
    }

    fun saveGoal(name: String, description: String, targetAmount: Double, period: String) {
        val newGoal = FinancialGoal(
            id = System.currentTimeMillis().toString(),
            name = name,
            description = description,
            targetAmount = targetAmount,
            currentAmount = 0.0,
            period = period
        )

        _goals.value = _goals.value + newGoal
        saveGoals()
        _showNewGoalDialog.value = false
    }

    private fun saveGoals() {
        // In a real app, you would convert the list of FinancialGoal objects to JSON
        // and save it to SharedPreferences
        sharedPreferences.edit().apply {
            // Save goals count for simplicity
            putInt("goals_count", _goals.value.size)
            apply()
        }
    }

    fun showNewGoalDialog() {
        _showNewGoalDialog.value = true
    }

    fun hideNewGoalDialog() {
        _showNewGoalDialog.value = false
    }

    fun updateGoalProgress(goalId: String, additionalAmount: Double) {
        val updatedGoals = _goals.value.map { goal ->
            if (goal.id == goalId) {
                val newAmount = goal.currentAmount + additionalAmount
                goal.copy(currentAmount = newAmount)
            } else {
                goal
            }
        }
        _goals.value = updatedGoals

        // Update achieved goals count
        _totalGoalsAchieved.value = updatedGoals.count { it.currentAmount >= it.targetAmount }

        saveGoals()
    }

    fun updateUserBalance(newBalance: Double) {
        _userBalance.value = newBalance
        sharedPreferences.edit().putFloat("user_balance", newBalance.toFloat()).apply()
    }

    fun addPoints(points: Int) {
        _totalPoints.value += points
        sharedPreferences.edit().putInt("total_points", _totalPoints.value).apply()
    }

    fun getGoalById(goalId: String): FinancialGoal? {
        return _goals.value.find { it.id == goalId }
    }
}

@Composable
fun GoalsScreen(
    onNavigateToQuiz: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel = remember { GoalsViewModel(context) }
    val scope = rememberCoroutineScope()

    val goals by viewModel.goals.collectAsState()
    val showNewGoalDialog by viewModel.showNewGoalDialog.collectAsState()
    val totalGoalsAchieved by viewModel.totalGoalsAchieved.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Suas Metas",
                color = Color(0xFFFFBF1C),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Stats cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Goals achieved card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFFFFBF1C),
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF333333), CircleShape)
                                .padding(6.dp)
                        )

                        Text(
                            text = "Metas Atingidas",
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = "$totalGoalsAchieved / 10",
                            color = Color(0xFFFFBF1C),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Points card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFFFFBF1C),
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF333333), CircleShape)
                                .padding(6.dp)
                        )

                        Text(
                            text = "Pontos Ganhos",
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = "+$totalPoints",
                            color = Color(0xFFFFBF1C),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Goals section title
            Text(
                text = "Suas metas",
                color = Color(0xFFFFBF1C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Goals list
            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Você ainda não tem metas.\nCrie sua primeira meta financeira!",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(goals) { goal ->
                        GoalItem(
                            goal = goal,
                            onClick = { onNavigateToQuiz(goal.id) }
                        )
                    }
                }
            }

            // Add goal button
            Button(
                onClick = { viewModel.showNewGoalDialog() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFBF1C),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Nova Meta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Logout button
            TextButton(
                onClick = { /* Implement logout */ },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Sair",
                    fontSize = 16.sp
                )
            }
        }

        // New goal dialog
        AnimatedVisibility(
            visible = showNewGoalDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NewGoalDialog(
                onDismiss = { viewModel.hideNewGoalDialog() },
                onCreateGoal = { name, description, amount, period ->
                    scope.launch {
                        viewModel.saveGoal(name, description, amount, period)
                    }
                }
            )
        }
    }
}

@Composable
fun GoalItem(
    goal: FinancialGoal,
    onClick: () -> Unit
) {
    val progress = (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
    val progressPercent = (progress * 100).toInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1A1A1A))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Goal icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF333333), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = Color(0xFFFFBF1C),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Goal info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = goal.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Progress percentage
        Text(
            text = "$progressPercent%",
            color = Color(0xFFFFBF1C),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Arrow icon
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalDialog(
    onDismiss: () -> Unit,
    onCreateGoal: (String, String, Double, String) -> Unit
) {
    var goalName by remember { mutableStateOf("") }
    var goalDescription by remember { mutableStateOf("") }
    var goalAmount by remember { mutableStateOf("") }
    var goalPeriod by remember { mutableStateOf("Dia") }
    var expanded by remember { mutableStateOf(false) }
    val periods = listOf("Dia", "Semana", "Mês", "Ano")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.Black
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nova Meta",
                        color = Color(0xFFFFBF1C),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color(0xFFFFBF1C)
                        )
                    }
                }

                // Target icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(width = 8.dp, color = Color(0xFF4C4C4C), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFFFBF1C), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color.Black, CircleShape)
                            )
                        }
                    }
                }

                // Form fields
                Text(
                    text = "Nome da meta:",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    placeholder = { Text("De um título para sua meta", color = Color.Gray) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color(0xFFFFBF1C),
                        focusedBorderColor = Color(0xFFFFBF1C),
                        unfocusedBorderColor = Color.Gray
                    ),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Descrição",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = goalDescription,
                    onValueChange = { goalDescription = it },
                    placeholder = { Text("Descreva melhor sua meta", color = Color.Gray) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color(0xFFFFBF1C),
                        focusedBorderColor = Color(0xFFFFBF1C),
                        unfocusedBorderColor = Color.Gray
                    ),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Valor a ser economizado",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = goalAmount,
                    onValueChange = { goalAmount = it },
                    placeholder = { Text("00,00", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color(0xFFFFBF1C),
                        focusedBorderColor = Color(0xFFFFBF1C),
                        unfocusedBorderColor = Color.Gray
                    ),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Período de Análise",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Dropdown for period selection
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    OutlinedTextField(
                        value = goalPeriod,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = Color(0xFFFFBF1C),
                            focusedBorderColor = Color(0xFFFFBF1C),
                            unfocusedBorderColor = Color.Gray,
                            containerColor = Color.Transparent
                        ),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF1A1A1A))
                    ) {
                        periods.forEach { period ->
                            DropdownMenuItem(
                                text = { Text(period, color = Color.White) },
                                onClick = {
                                    goalPeriod = period
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Create goal button
                Button(
                    onClick = {
                        val amount = goalAmount.replace(",", ".").toDoubleOrNull() ?: 0.0
                        onCreateGoal(goalName, goalDescription, amount, goalPeriod)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFBF1C),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Criar Meta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

