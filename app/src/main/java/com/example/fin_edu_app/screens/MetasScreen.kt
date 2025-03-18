package com.example.fin_edu_app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon


// Dados de exemplo
data class MetaItem(
 val nome: String,
 val progresso: Float
)

@Composable
fun SuasMetasScreen() {
 // Cores principais
 val backgroundBrush = Brush.verticalGradient(
  0f to Color(0xFF321C0B),  // Marrom final no topo (0%)
  0.05f to Color(0xFF311B0B), // Marrom escuro suavizado até 8%
  0.09f to Color(0xFF1A0D05), // Transição gradual para preto com um tom intermediário
  0.2f to Color(0xFF090403), // Preto suavizado para evitar transições abruptas
  1f to Color(0xFF000000)  // Preto total no final (100%)

 )

  val goldColor = Color(0xFFFFD700)      // Dourado mais fiel à imagem
 val cardColor = Color(0xFF1E1E1E)      // Fundo dos cartões
 val navBarColor = Color(0xFF212121)    // Cor da barra de navegação

 // Metas fictícias
 val listaMetas = listOf(
  MetaItem("Alimentação no dia a dia", 0.25f),
  MetaItem("Alimentação no dia a dia", 0.25f),
  MetaItem("Alimentação no dia a dia", 0.25f),
  MetaItem("Alimentação no dia a dia", 0.25f),
  MetaItem("Alimentação no dia a dia", 0.25f),
  MetaItem("Alimentação no dia a dia", 0.25f),

  )

 // Scaffold com FAB central e BottomAppBar
 Scaffold(
  containerColor = Color.Black,
  floatingActionButton = {
   FloatingActionButton(
    onClick = { /* Ação do FAB */ },
    containerColor = goldColor,
    contentColor = Color.Black,
    modifier = Modifier.offset(y = 55.dp), // Desce 50px
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
      .padding(top = 5.dp), // Padding aplicado corretamente
     horizontalArrangement = Arrangement.SpaceAround
    ) {
     // Ícone Home
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

     // Espaço para o FAB
     Spacer(modifier = Modifier.weight(1f))

     // Ícone Mensagens (substituído por Email)
     IconButton(
      onClick = { /* Navegar para Mensagens */ },
      modifier = Modifier.weight(1f)
     ) {
      Icon(
       imageVector = Icons.Filled.ChatBubble, // Ícone de troféu
       contentDescription = "Quiz",
       tint = goldColor,
       modifier = Modifier.size(35.dp)
      )
     }
    }
   }
  }
 ) { innerPadding ->
  // Conteúdo principal
  Box(
   modifier = Modifier
    .padding(innerPadding)
    .fillMaxSize()
    .background(backgroundBrush))
   {
   Column(
    modifier = Modifier.padding(horizontal = 16.dp)
   ) {
    // Título principal
    Spacer(modifier = Modifier.height(16.dp))
    Text(
     text = "Suas Metas",
     color = Color.White,
     fontSize = 22.sp,
     fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Cards de Metas Atingidas e Pontos Ganhos lado a lado
    Row(
     modifier = Modifier.fillMaxWidth(),
     horizontalArrangement = Arrangement.SpaceBetween
    ) {
     // Card 1: Metas Atingidas
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
       // Ícone de troféu
       Icon(
        imageVector = Icons.Filled.EmojiEvents, // Ícone de medalha/troféu
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
        text = "3 / 10",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
       )
      }
     }

     Spacer(modifier = Modifier.width(16.dp))

     // Card 2: Pontos Ganhos
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
       // Ícone de medalha
       Icon(
        imageVector = Icons.Filled.WorkspacePremium , // Ícone de medalha/troféu
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
        text = "+3400",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
       )
      }
     }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Subtítulo
    Text(
     text = "Suas metas",
     color = goldColor,
     fontSize = 16.sp,
     fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Lista de metas
    LazyColumn(
     verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
     items(listaMetas) { meta ->
      MetaRowItem(meta, goldColor)
     }
    }
   }
  }
 }
}

// Item da lista de metas
@Composable
fun MetaRowItem(meta: MetaItem, accentColor: Color) {
 // Cada linha
 Row(
  modifier = Modifier
   .fillMaxWidth()
   .clip(RoundedCornerShape(12.dp))
   .background(Color(0xFF1E1E1E00))
   .padding(horizontal = 16.dp, vertical = 16.dp),
  verticalAlignment = Alignment.CenterVertically
 ) {
  // Ícone dentro de um círculo
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

  // Texto da meta
  Text(
   text = meta.nome,
   color = Color.White,
   fontSize = 17.sp,
   modifier = Modifier.weight(1f)
  )

  // Porcentagem
  Text(
   text = "25%",
   color = Color.White ,
   fontWeight = FontWeight.Bold,
   fontSize = 20.sp
  )

  Spacer(modifier = Modifier.width(8.dp))

  // Seta
  Icon(
   imageVector = Icons.Default.ArrowForward,
   contentDescription = "Ir",
   tint = accentColor,
   modifier = Modifier.size(18.dp)
  )
 }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSuasMetasScreen() {
 SuasMetasScreen()
}