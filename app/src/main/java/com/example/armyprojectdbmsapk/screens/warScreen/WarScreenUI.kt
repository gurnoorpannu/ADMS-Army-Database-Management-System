package com.example.armyprojectdbmsapk.screens.warScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.armyprojectdbmsapk.model.War
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.style.TextAlign

@Composable
fun WarScreen(
    viewModel: WarScreenViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val pastWars by remember { viewModel.pastWars }
    val ongoingWars by remember { viewModel.ongoingWars }
    val isLoading by remember { viewModel.isLoading }
    val error by remember { viewModel.error }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title row with back arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Text(
                    text = "WAR DETAILS",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                // Empty box for symmetry
                Spacer(modifier = Modifier.size(48.dp))
            }

            // Tabs for Past and Ongoing Wars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedTab = 0 },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) Color.White 
                                       else Color.Gray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Past Wars")
                }
                Button(
                    onClick = { selectedTab = 1 },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) Color.White 
                                       else Color.Gray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Ongoing Wars")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                error != null -> {
                    Text(
                        text = error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn {
                        when (selectedTab) {
                            0 -> { // Past Wars
                                if (pastWars.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Past Wars",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            color = Color.White
                                        )
                                    }
                                    items(pastWars) { war ->
                                        WarCard(war = war)
                                    }
                                }
                            }
                            1 -> { // Ongoing Wars
                                if (ongoingWars.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Ongoing Wars",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            color = Color.White
                                        )
                                    }
                                    items(ongoingWars) { war ->
                                        WarCard(war = war)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WarCard(war: War) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray // Dark card background
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "WAR ${war.dateNo}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = war.dateNo,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Location(Pincode): ${war.pincode}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}