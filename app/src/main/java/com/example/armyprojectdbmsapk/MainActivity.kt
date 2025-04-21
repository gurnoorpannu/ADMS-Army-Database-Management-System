package com.example.armyprojectdbmsapk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.armyprojectdbmsapk.screens.soldierDetailScreen.SoldierDetailsScreen
import com.example.armyprojectdbmsapk.screens.warScreen.WarScreen
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
                    // Call the ADMS UI screen we created
                   // SoldierDetailsScreen()
                    WarScreen()
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