package com.example.armyprojectdbmsapk.screens.battalionDetailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.armyprojectdbmsapk.R

@Composable
fun BattalionDetailScreen(
    viewModel: BattalionDetailScreenViewModel,
    battalionId: String,
    onBackPressed: () -> Unit
) {
    // Observe state from ViewModel
    val battalionDetail by viewModel.battalionDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    // Load data on first composition
    LaunchedEffect(battalionId) {
        viewModel.loadBattalionDetail(battalionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top app bar with back button and title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Battalion Details",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Heart icon in top-right corner
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF330000))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
        }

        // Show loading indicator when loading
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        // Show error if there is one
        else if (error != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = error ?: "Unknown error occurred",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = { viewModel.loadBattalionDetail(battalionId) }) {
                        Text("Retry")
                    }
                }
            }
        }
        // Show content when data is loaded
        else if (battalionDetail != null) {
            // Main content in LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Battalion image
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF6200EE).copy(alpha = 0.2f))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF6200EE),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.battalion),
                                contentDescription = "Battalion Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    }
                }

                // Battalion name and details
                item {
                    Text(
                        text = battalionDetail?.battalion_name ?: "",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Swadharme Nidhanam Shreyah",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Text(
                        text = "Line Infantry / Counterinsurgency / Conventional Warfare",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                // More details section
                item {
                    Text(
                        text = "MORE DETAILS",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Detail items
                item {
                    DetailItem(
                        title = "Captain ID",
                        value = battalionDetail?.captainId?.toString() ?: ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    DetailItem(
                        title = "Year",
                        value = battalionDetail?.year?.toString() ?: ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    DetailItem(
                        title = "Total Capacity",
                        value = battalionDetail?.totalCapacity?.toString() ?: ""
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF171717), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                // Purple vertical indicator
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(24.dp)
                        .background(Color(0xFF6200EE), shape = RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                )
            }

            Text(
                text = value,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}