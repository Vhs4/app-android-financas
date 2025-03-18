package com.example.fin_edu_app.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Dados de exemplo
data class MetaItem(
 val nome: String,
 val progresso: Float
)

class MetasViewModel(private val context: Context) {
 private val sharedPreferences = context.getSharedPreferences("financial_goals", Context.MODE_PRIVATE)

 private val _metas = MutableStateFlow<List<MetaItem>>(emptyList())
 val metas: StateFlow<List<MetaItem>> = _metas.asStateFlow()

 private val _showNewGoalDialog = MutableStateFlow(false)
 val showNewGoalDialog: StateFlow<Boolean> = _showNewGoalDialog.asStateFlow()

 private val _totalGoalsAchieved = MutableStateFlow(3)
 val totalGoalsAchieved: StateFlow<Int> = _totalGoalsAchieved.asStateFlow()

 private val _totalPoints = MutableStateFlow(3400)
 val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

 init {
  loadMetas()
 }

 private fun loadMetas() {
  // Carregar metas salvas ou usar metas fictícias
  _metas.value = listOf(
   MetaItem("Alimentação no dia a dia", 0.25f),
   MetaItem("Alimentação no dia a dia", 0.25f),
   MetaItem("Alimentação no dia a dia", 0.25f),
   MetaItem("Alimentação no dia a dia", 0.25f),
   MetaItem("Alimentação no dia a dia", 0.25f),
   MetaItem("Alimentação no dia a dia", 0.25f)
  )
 }

 fun showNewGoalDialog() {
  _showNewGoalDialog.value = true
 }

 fun hideNewGoalDialog() {
  _showNewGoalDialog.value = false
 }

 fun saveMeta(nome: String, descricao: String, valor: Double, periodo: String) {
  // Adicionar nova meta à lista
  val novaMeta = MetaItem(nome, 0.0f)
  _metas.value = _metas.value + novaMeta

  // Salvar metas (implementação simplificada)
  sharedPreferences.edit().apply {
   putInt("metas_count", _metas.value.size)
   apply()
  }

  // Fechar diálogo
  _showNewGoalDialog.value = false
 }
}

@Composable
fun SuasMetasScreen(navController: NavController) {
 // ViewModel para gerenciar o estado
 val context = LocalContext.current
 val viewModel = remember { MetasViewModel(context) }
 val scope = rememberCoroutineScope()

 // Coletar estados do ViewModel
 val metas by viewModel.metas.collectAsState()
 val showNewGoalDialog by viewModel.showNewGoalDialog.collectAsState()
 val totalGoalsAchieved by viewModel.totalGoalsAchieved.collectAsState()
 val totalPoints by viewModel.totalPoints.collectAsState()

 // Cores principais
 val backgroundBrush = Brush.verticalGradient(
  0f to Color(0xFF321C0B),
  0.05f to Color(0xFF311B0B),
  0.09f to Color(0xFF1A0D05),
  0.2f to Color(0xFF090403),
  1f to Color(0xFF000000)
 )

 val goldColor = Color(0xFFFFD700)
 val cardColor = Color(0xFF1E1E1E)
 val navBarColor = Color(0xFF212121)

 // Scaffold com FAB central e BottomAppBar
 Scaffold(
  containerColor = Color.Black,
  floatingActionButton = {
   FloatingActionButton(
    onClick = { viewModel.showNewGoalDialog() },
    containerColor = goldColor,
    contentColor = Color.Black,
    modifier = Modifier.offset(y = 55.dp),
   ) {
    Icon(
     Icons.Default.Add,
     contentDescription = "Adicionar",
     modifier = Modifier.size(24.dp)
    )
   }
  },
  floatingActionButtonPosition = FabPosition.Center,
  bottomBar = {
   BottomAppBar(
    containerColor = navBarColor,
    contentColor = goldColor,
    tonalElevation = 8.dp,
    modifier = Modifier.height(60.dp)
   ) {
    Row(
     modifier = Modifier
      .fillMaxWidth()
      .padding(top = 5.dp),
     horizontalArrangement = Arrangement.SpaceAround
    ) {
     IconButton(
      onClick = { /* Navegar para Home */ },
      modifier = Modifier.weight(1f)
     ) {
      Icon(
       imageVector = Icons.Default.Home,
       contentDescription = "Home",
       tint = goldColor,
       modifier = Modifier.size(60.dp)
      )
     }

     Spacer(modifier = Modifier.weight(1f))

     IconButton(
      onClick = { /* Navegar para Mensagens */ },
      modifier = Modifier.weight(1f)
     ) {
      Icon(
       imageVector = Icons.Filled.Create,
       contentDescription = "Quiz",
       tint = goldColor,
       modifier = Modifier.size(35.dp)
      )
     }
    }
   }
  }
 ) { innerPadding ->
  Box(
   modifier = Modifier
    .padding(innerPadding)
    .fillMaxSize()
    .background(backgroundBrush)
  ) {
   Column(
    modifier = Modifier.padding(horizontal = 16.dp)
   ) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
     text = "Suas Metas",
     color = Color.White,
     fontSize = 22.sp,
     fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
     modifier = Modifier.fillMaxWidth(),
     horizontalArrangement = Arrangement.SpaceBetween
    ) {
     Card(
      modifier = Modifier
       .weight(1f)
       .height(110.dp),
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(1.dp, Color(0xFF333333)),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent)
     ) {
      Column(
       modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
      ) {
       Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = "Troféu",
        tint = goldColor,
        modifier = Modifier.size(40.dp)
       )
       Spacer(modifier = Modifier.height(8.dp))
       Text(
        text = "Metas Atingidas",
        color = Color.White,
        fontSize = 12.sp
       )
       Spacer(modifier = Modifier.height(4.dp))
       Text(
        text = "$totalGoalsAchieved / 10",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
       )
      }
     }

     Spacer(modifier = Modifier.width(16.dp))

     Card(
      modifier = Modifier
       .weight(1f)
       .height(110.dp),
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(1.dp, Color(0xFF333333)),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent)
     ) {
      Column(
       modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
      ) {
       Icon(
        imageVector = Icons.Filled.CheckCircle,
        contentDescription = "Medalha",
        tint = goldColor,
        modifier = Modifier.size(40.dp)
       )
       Spacer(modifier = Modifier.height(8.dp))
       Text(
        text = "Pontos Ganhos",
        color = Color.White,
        fontSize = 12.sp
       )
       Spacer(modifier = Modifier.height(4.dp))
       Text(
        text = "+$totalPoints",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
       )
      }
     }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
     onClick = { navController.navigate("quiz") },
     modifier = Modifier
      .fillMaxWidth()
      .height(56.dp),
     colors = ButtonDefaults.buttonColors(
      containerColor = goldColor,
      contentColor = Color.Black
     ),
     shape = RoundedCornerShape(12.dp)
    ) {
     Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
     ) {
      Icon(
       imageVector = Icons.Filled.PlayArrow,
       contentDescription = "Iniciar",
       modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
       text = "Iniciar jogo",
       fontSize = 18.sp,
       fontWeight = FontWeight.Bold
      )
     }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
     text = "Suas metas",
     color = goldColor,
     fontSize = 16.sp,
     fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
     verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
     items(metas) { meta ->
      MetaRowItem(meta, goldColor)
     }
    }
   }

   // Diálogo para adicionar nova meta (mostrado quando showNewGoalDialog é verdadeiro)
   AnimatedVisibility(
    visible = showNewGoalDialog,
    enter = fadeIn(),
    exit = fadeOut()
   ) {
    NewGoalDialog(
     onDismiss = { viewModel.hideNewGoalDialog() },
     onCreateGoal = { nome, descricao, valor, periodo ->
      scope.launch {
       viewModel.saveMeta(nome, descricao, valor, periodo)
      }
     },
     goldColor = goldColor
    )
   }
  }
 }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalDialog(
 onDismiss: () -> Unit,
 onCreateGoal: (String, String, Double, String) -> Unit,
 goldColor: Color
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
    // Cabeçalho com título e botão fechar
    Row(
     modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 24.dp),
     horizontalArrangement = Arrangement.SpaceBetween,
     verticalAlignment = Alignment.CenterVertically
    ) {
     Text(
      text = "Nova Meta",
      color = goldColor,
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold
     )

     IconButton(onClick = onDismiss) {
      Icon(
       imageVector = Icons.Default.Close,
       contentDescription = "Fechar",
       tint = goldColor
      )
     }
    }

    // Ícone alvo
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
        .background(goldColor, CircleShape),
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

    // Campos do formulário
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
      cursorColor = goldColor,
      focusedBorderColor = goldColor,
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
      cursorColor = goldColor,
      focusedBorderColor = goldColor,
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
      cursorColor = goldColor,
      focusedBorderColor = goldColor,
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

    // Dropdown para seleção de período
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
       cursorColor = goldColor,
       focusedBorderColor = goldColor,
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

    // Botão para criar meta
    Button(
     onClick = {
      val amount = goalAmount.replace(",", ".").toDoubleOrNull() ?: 0.0
      onCreateGoal(goalName, goalDescription, amount, goalPeriod)
     },
     colors = ButtonDefaults.buttonColors(
      containerColor = goldColor,
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
      fontWeight = FontWeight.Bold
     )
    }
   }
  }
 }
}

// Item da lista de metas
@Composable
fun MetaRowItem(meta: MetaItem, accentColor: Color) {
 Row(
  modifier = Modifier
   .fillMaxWidth()
   .clip(RoundedCornerShape(12.dp))
   .background(Color(0xFF1E1E1E00))
   .padding(horizontal = 16.dp, vertical = 16.dp),
  verticalAlignment = Alignment.CenterVertically
 ) {
  Box(
   modifier = Modifier
    .size(36.dp)
    .clip(RoundedCornerShape(10.dp))
    .background(Color(0xFF1E1E1E)),
   contentAlignment = Alignment.Center
  ) {
   Icon(
    imageVector = Icons.Default.Check,
    contentDescription = "Meta Icon",
    tint = accentColor,
    modifier = Modifier.size(20.dp)
   )
  }

  Spacer(modifier = Modifier.width(16.dp))

  Text(
   text = meta.nome,
   color = Color.White,
   fontSize = 17.sp,
   modifier = Modifier.weight(1f)
  )

  Text(
   text = "25%",
   color = Color.White ,
   fontWeight = FontWeight.Bold,
   fontSize = 20.sp
  )

  Spacer(modifier = Modifier.width(8.dp))

  Icon(
   imageVector = Icons.Default.ArrowForward,
   contentDescription = "Ir",
   tint = accentColor,
   modifier = Modifier.size(18.dp)
  )
 }
}