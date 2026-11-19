package com.alazhartex.costing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.alazhartex.costing.di.AppContainer
import com.alazhartex.costing.ui.navigation.AppNavHost
import com.alazhartex.costing.ui.theme.AzharTexCostingTheme

class MainActivity : ComponentActivity() {

    private val container: AppContainer
        get() = (application as CostingApplication).container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AzharTexCostingTheme {
                Scaffold { innerPadding ->
                    AppNavHost(
                        container = container,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
