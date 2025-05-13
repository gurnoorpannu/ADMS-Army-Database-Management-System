package com.example.armyprojectdbmsapk.screens.weaponDetailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.armyprojectdbmsapk.R

@Composable
fun WeaponDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: WeaponDetailsViewModel = viewModel()
) {
    val weaponState by viewModel.weaponState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header with back button is always visible
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            // Row for back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Weapon Details",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

            Box(
            modifier = Modifier
                .fillMaxSize() // Takes full screen space
        ) {
            // Heart icon in top-right corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd) // Push to top right
                    .padding(16.dp)          // Optional: space from screen edges
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF330000))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { viewModel.toggleFavorite() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            }
        }
    }

        // Show loading indicator or content based on loading state
    if (isLoading) {
        // Center the loading indicator
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color(0xFF6200EE)
            )
        }
    } else {
        // Main content - only shown when not loading
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Weapon image with purple border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF4B2D5C))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    val painter = if (weaponState.imageURL.isNullOrEmpty()) {
                        painterResource(id = R.drawable.gun)
                    } else {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(weaponState.imageURL)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.gun),
                            error = painterResource(id = R.drawable.gun)
                        )
                    }

                    Image(
                        painter = painter,
                        contentDescription = weaponState.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            // Weapon name and category
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = weaponState.name,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Text(
                    text = weaponState.category ?: "Assault Rifle",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weaponState.description ?: "Iconic, rugged, weapon known for reliability and impact.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
            }

            // More details header
            Text(
                text = "MORE DETAILS",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )

            // Details cards
            InfoCard(
                title = "Weapon ID",
                content = weaponState.weaponId?.toString() ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Caliber",
                content = weaponState.caliber?.toString() ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Range",
                content = weaponState.range ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Total Available Quantity",
                content = weaponState.totalQuantity?.toString() ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Manufacturer",
                content = weaponState.manufacturer ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Manufacturer Date",
                content = weaponState.manufacturerDate ?: ""
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun InfoCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(Color(0xFF171717), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Purple vertical indicator
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(24.dp)
                        .background(Color(0xFF6200EE), shape = RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                ),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}