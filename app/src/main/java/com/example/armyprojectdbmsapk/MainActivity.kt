package com.example.armyprojectdbmsapk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.armyprojectdbmsapk.screens.ADMSApp
import com.example.armyprojectdbmsapk.screens.battalionScreen.BattalionListViewModel
import com.example.armyprojectdbmsapk.screens.battalionScreen.BattalionsScreen
import com.example.armyprojectdbmsapk.screens.databaseScreen.ExploreDatabaseScreen
import com.example.armyprojectdbmsapk.screens.soldierDetailScreen.SoldierDetailsScreen
import com.example.armyprojectdbmsapk.screens.soldierDetailScreen.SoldierViewModel
import com.example.armyprojectdbmsapk.screens.warScreen.WarScreen
import com.example.armyprojectdbmsapk.screens.warScreen.WarScreenViewModel
import com.example.armyprojectdbmsapk.screens.weaponDetailScreen.WeaponDetailsScreen
import com.example.armyprojectdbmsapk.screens.weaponDetailScreen.WeaponDetailsViewModel
import com.example.armyprojectdbmsapk.ui.theme.ArmyProjectDBMSApkTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Test Firebase connection
        testFirebaseConnection()

        setContent {
            ArmyProjectDBMSApkTheme {
                // Using a Surface as the root container to apply the theme's background color
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Set up the navigation controller and start the NavHost
                    val navController = rememberNavController()
                    ArmyNavHost(navController = navController)
                }
            }
        }
    }

    private fun testFirebaseConnection() {
        Log.d(TAG, "Testing Firebase connection...")
        val db = FirebaseFirestore.getInstance()

        // Test if we can access Firestore at all
        db.collection("soldier")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Firebase connection successful!")
                Log.d(TAG, "Documents found: ${documents.size()}")
                for (document in documents) {
                    Log.d(TAG, "Document: ${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Firebase connection failed: ${e.message}", e)
            }
    }
}

/**
 * Main navigation component for the app
 */
@Composable
fun ArmyNavHost(
    navController: NavHostController = rememberNavController()
) {
    // Create shared ViewModels for each screen
    val weaponDetailsViewModel: WeaponDetailsViewModel = viewModel()
    val soldierDetailsViewModel: SoldierViewModel = viewModel()
    val warScreenViewModel: WarScreenViewModel = viewModel()
    val battalionListViewModel: BattalionListViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home Screen (ADMSApp)
        composable("home") {
            ADMSApp(
                onExploreDatabase = {
                    navController.navigate("database")
                }
            )
        }
        // Explore Database Screen
        composable("database") {
            ExploreDatabaseScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onActiveWarsClick = {
                    navController.navigate("wars")
                },
                onBattalionsClick = {
                    navController.navigate("battalions")
                },
                onSearch = { query, isWeapon ->
                    if (isWeapon) {
                        // Search for weapon
                        weaponDetailsViewModel.loadWeaponById(query)
                        navController.navigate("weaponDetails")
                    } else {
                        // Search for soldier
                        soldierDetailsViewModel.loadSoldierById(query)
                        navController.navigate("soldierDetails")
                    }
                }
            )
        }

        // Weapon Details Screen
        composable(
            route = "weaponDetails",
        ) {
            WeaponDetailsScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                viewModel = weaponDetailsViewModel,
            )
        }

        // Soldier Details Screen
        composable(
            route = "soldierDetails",
        ) {
            SoldierDetailsScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                viewModel = soldierDetailsViewModel
            )
        }

        // Wars Screen
        composable("wars") {
            WarScreen(
                viewModel = warScreenViewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onWarClick = { warId ->
                    // Navigate to war details if needed
                    navController.navigate("warDetails/$warId")
                }
            )
        }

        // War Details Screen
        composable(
            route = "warDetails/{warId}",
            arguments = listOf(navArgument("warId") { type = NavType.StringType })
        ) { backStackEntry ->
            val warId = backStackEntry.arguments?.getString("warId") ?: ""
            // Implement War Details Screen here
            Text(text = "War Details for War ID: $warId")
        }

        // Battalions List Screen
        composable("battalions") {
            BattalionsScreen(
                viewModel = battalionListViewModel,
                onBattalionClick = { battalion ->
                    // Navigate to battalion details screen
                    navController.navigate("battalionDetail/${battalion.id}")
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        // Battalion Detail Screen
//        composable(
//            route = "battalionDetail/{battalionId}",
//            arguments = listOf(navArgument("battalionId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val battalionId = backStackEntry.arguments?.getString("battalionId") ?: ""
//            BattalionsScreen(
//                battalionId = battalionId,
//                onBackClick = {
//                    navController.navigateUp()
//                }
//            )
//        }
    }
}

/**
 * Unified search bar component that can search for both weapons and soldiers
 */
@Composable
fun UnifiedSearchBar(
    onSearch: (query: String, isWeapon: Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    var isWeaponSearch by remember { mutableStateOf(true) } // Default to weapon search
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Search Type Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Toggle buttons for search type
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Button(
                    onClick = { isWeaponSearch = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isWeaponSearch)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Weapon ID")
                }

                Button(
                    onClick = { isWeaponSearch = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isWeaponSearch)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Soldier ID")
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                isError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    if (isWeaponSearch) "Search weapon by ID" else "Search soldier by ID"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                Button(
                    onClick = {
                        performSearch(
                            searchQuery = searchQuery,
                            onInvalidInput = { message ->
                                isError = true
                                errorMessage = message
                            },
                            onSearch = { id ->
                                onSearch(id, isWeaponSearch)
                                focusManager.clearFocus()
                            }
                        )
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Search")
                }
            },
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    performSearch(
                        searchQuery = searchQuery,
                        onInvalidInput = { message ->
                            isError = true
                            errorMessage = message
                        },
                        onSearch = { id ->
                            onSearch(id, isWeaponSearch)
                            focusManager.clearFocus()
                        }
                    )
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )

        // Error message
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

private fun performSearch(
    searchQuery: String,
    onInvalidInput: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    when {
        searchQuery.isBlank() -> {
            onInvalidInput("Please enter an ID")
        }
        // For simplicity, let's assume all IDs have alphanumeric format
        // You can add more specific validation for weapon vs soldier IDs
        else -> {
            onSearch(searchQuery)
        }
    }
}