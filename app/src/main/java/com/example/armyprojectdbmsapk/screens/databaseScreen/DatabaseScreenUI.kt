package com.example.armyprojectdbmsapk.screens.databaseScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.armyprojectdbmsapk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreDatabaseScreen(
    onBackClick: () -> Unit,
    onActiveWarsClick: () -> Unit,
    onBattalionsClick: () -> Unit,
    onSearch: (query: String, isWeapon: Boolean) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isWeaponSearch by remember { mutableStateOf(true) } // Default to weapon search
    var showSearchTypeOptions by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Explore Database",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )

            // Search Type Toggle
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF212121))
                ) {
                    // Weapon search toggle
                    Box(
                        modifier = Modifier
                            .clickable { isWeaponSearch = true }
                            .background(if (isWeaponSearch) Color(0xFF4CAF50) else Color(0xFF212121))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Weapon ID",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Soldier search toggle
                    Box(
                        modifier = Modifier
                            .clickable { isWeaponSearch = false }
                            .background(if (!isWeaponSearch) Color(0xFF4CAF50) else Color(0xFF212121))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Soldier ID",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        if (isWeaponSearch) "Search weapon by ID" else "Search soldier by ID",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    Button(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                onSearch(searchQuery, isWeaponSearch)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Search")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFF212121),
                    unfocusedBorderColor = Color(0xFF333333),
                    focusedBorderColor = Color(0xFF555555),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                textStyle = TextStyle(color = Color.White),
                singleLine = true
            )

            // Statistics Cards - First Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Soldiers Card
                StatCardWithImage(
                    title = "Total Soldiers",
                    value = "24,681",
                    imageRes = R.drawable.soldier,
                    valueColor = Color(0xFF4CAF50),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(end = 8.dp)
                )

                // Active Wars Card with Arrow
                StatCardWithImageAndArrow(
                    title = "Active Wars",
                    value = "8",
                    imageRes = R.drawable.swords,
                    valueColor = Color(0xFFFF5252),
                    onClick = onActiveWarsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(start = 8.dp)
                )
            }

            // Statistics Cards - Second Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Weapons Card
                StatCardWithImage(
                    title = "Total Weapons",
                    value = "56,892",
                    imageRes = R.drawable.total_weapons,
                    valueColor = Color(0xFF2196F3),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(end = 8.dp)
                )

                // Battalions Card with Arrow
                StatCardWithImageAndArrow(
                    title = "Battalions",
                    value = "142",
                    imageRes = R.drawable.shield,
                    valueColor = Color(0xFFFFEB3B),
                    onClick = onBattalionsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(start = 8.dp)
                )
            }

            // Recent Activities Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF212121))
                    .padding(bottom = 16.dp)
            ) {
                Column {
                    // Title
                    Text(
                        text = "RECENT ACTIVITIES:-",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(16.dp)
                    )

                    // Activity Items
                    ActivityItem(
                        activity = "Raj Verma Got Promoted",
                        date = "02/04/2025"
                    )
                    ActivityItem(
                        activity = "Vikram Singh Got Retired",
                        date = "13/03/2025"
                    )
                    ActivityItem(
                        activity = "Captain Disha Meets PM",
                        date = "02/12/2024"
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Indicator
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun StatCardWithImage(
    title: String,
    value: String,
    imageRes: Int,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF212121))
            .padding(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = TextStyle(
                    color = valueColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun StatCardWithImageAndArrow(
    title: String,
    value: String,
    imageRes: Int,
    valueColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF212121))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Navigate",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = TextStyle(
                    color = valueColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun ActivityItem(
    activity: String,
    date: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF4CAF50).copy(alpha = 0.7f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )

            Text(
                text = date,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
        }
    }
}