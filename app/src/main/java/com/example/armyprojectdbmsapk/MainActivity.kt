
package com.example.armyprojectdbmsapk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.armyprojectdbmsapk.ui.theme.ArmyProjectDBMSApkTheme
import com.example.armyprojectdbmsapk.screens.ADMSApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArmyProjectDBMSApkTheme {
                // Using a Surface as the root container to apply the theme's background color
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Call the ADMS UI screen we created
                    ADMSApp()
                }
            }
        }
    }
}