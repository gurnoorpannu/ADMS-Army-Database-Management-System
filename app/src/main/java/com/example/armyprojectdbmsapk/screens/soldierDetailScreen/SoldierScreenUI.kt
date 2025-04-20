package com.example.armyprojectdbmsapk.screens.soldierDetailScreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.armyprojectdbmsapk.R
import com.example.armyprojectdbmsapk.model.Location
import com.example.armyprojectdbmsapk.model.Posting
import com.example.armyprojectdbmsapk.model.Soldier
import com.example.armyprojectdbmsapk.model.Visited
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SoldierDetailsScreen(
    viewModel: SoldierViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TopBar(onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    //Color = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                ),
                placeholder = { Text("Enter Soldier ID", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        coroutineScope.launch {
                            viewModel.searchSoldierById(searchQuery)
                            keyboardController?.hide()
                        }
                    }
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Button
            Button(
                onClick = {
                    viewModel.searchSoldierById(searchQuery)
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
            ) {
                Text("Search")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Loading indicator
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            
            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            
            // Content based on search result
            if (uiState.soldier != null && !isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    SoldierPhotoCard(uiState.soldier!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    KeyStatsSection(uiState.soldier!!, uiState.birthLocation)
                    Spacer(modifier = Modifier.height(16.dp))
                    PostingDetailsSection(uiState.postings)
                    Spacer(modifier = Modifier.height(16.dp))
                    VisitingDetailsSection(uiState.visits)
                    Spacer(modifier = Modifier.height(16.dp))
                    BodyDetailsTable(uiState.soldier!!)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else if (!isLoading && errorMessage == null) {
                // No search performed yet or no results
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Enter a soldier ID to view details",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TableHeader(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0x33FFFFFF))
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(600),
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
fun TopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "Soldier Details",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(700),
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun SoldierPhotoCard(soldier: Soldier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .border(
                width = 1.dp,
                color = Color(0x14FFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        // Background image
        Image(
            painter = painterResource(R.drawable.soldier_card),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )

        // Soldier details
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Text(
                text = "ID: ${soldier.id}",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = soldier.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
            )

            Text(
                text = soldier.rank,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFCCCDD7)
                )
            )
        }
    }
}


@Composable
fun KeyStatsSection(soldier: Soldier, birthLocation: Location?) {
    Column {
        Text(
            text = "Key stats",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(700),
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(title = "DATE OF JOINING", value = soldier.doj.split(" at ").firstOrNull() ?: soldier.doj)
            InfoBox(title = "SQUAD No.", value = soldier.squadNo)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(
                title = "BIRTH PLACE(PINCODE)",
                value = if (birthLocation != null) {
                    "${birthLocation.district}, ${birthLocation.state}"
                } else {
                    "${soldier.birthPlacePincode}"
                }
            )
            InfoBox(title = "GENDER", value = soldier.sex)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(title = "BASE PAY", value = "30000")
            InfoBox(title = "MEDALS", value = "2")
        }
    }
}

@Composable
fun PostingDetailsSection(postings: List<Posting>) {
    Column {
        Text(
            text = "Posting Details",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(700),
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (postings.isNotEmpty()) {
            // Show the most recent posting (assuming the list is sorted by date)
            val latestPosting = postings.first()
            
            InfoBox(
                title = "CURRENT POSTING",
                value = "Pincode: ${latestPosting.pincode}",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoBox(
                title = "DATE OF POSTING",
                value = latestPosting.date.split(" at ").firstOrNull() ?: latestPosting.date,
                modifier = Modifier.fillMaxWidth()
            )
            
            // If there are previous postings, show them as well
            if (postings.size > 1) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Previous Postings",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight(600),
                        color = Color.White
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                postings.drop(1).forEach { posting ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = posting.date.split(" at ").firstOrNull() ?: posting.date,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        
                        Text(
                            text = "Pincode: ${posting.pincode}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        } else {
            Text(
                text = "No posting records found",
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun VisitingDetailsSection(visits: List<Visited>) {
    Column {
        Text(
            text = "Visited Details",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(700),
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (visits.isNotEmpty()) {
            // Show the most recent visit (assuming the list is sorted by date)
            val latestVisit = visits.first()
            
            InfoBox(
                title = "LAST VISITED",
                value = "Pincode: ${latestVisit.pincode}",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoBox(
                title = "DATE OF VISIT",
                value = latestVisit.date.split(" at ").firstOrNull() ?: latestVisit.date,
                modifier = Modifier.fillMaxWidth()
            )
            
            // If there are previous visits, show them as well
            if (visits.size > 1) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Previous Visits",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight(600),
                        color = Color.White
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                visits.drop(1).forEach { visit ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = visit.date.split(" at ").firstOrNull() ?: visit.date,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        
                        Text(
                            text = "Pincode: ${visit.pincode}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        } else {
            Text(
                text = "No visit records found",
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun BodyDetailsTable(soldier: Soldier) {
    Column {
        Text(
            text = "Body Details",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight(700),
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Table header
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TableHeader(text = "Measurement", modifier = Modifier.weight(1f))
            TableHeader(text = "Value", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Table rows with actual soldier data
        TableRow(label = "Height", value = "${soldier.height} cm")
        TableRow(label = "Weight", value = "${soldier.weight} kg")
        TableRow(label = "Chest", value = "${soldier.chest} inches")
    }
}


@Composable
fun InfoBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier.width(160.dp)
) {
    Box(
        modifier = modifier
            .height(94.dp)
            .border(
                width = 1.dp,
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color(0x2EFFFFFF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9E9E9E)
                )
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = value,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
            )
        }
    }
}


@Composable
fun TableRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x33FFFFFF))
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = Color.White
                )
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
