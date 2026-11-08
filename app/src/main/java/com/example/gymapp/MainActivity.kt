package com.example.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.ui.AppViewModelProvider
import com.example.gymapp.ui.MainViewModel
import com.example.gymapp.ui.navigation.AppNavHost
import com.example.gymapp.ui.theme.GymappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymappTheme {
                val mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
                AppNavHost(startDestination = mainViewModel.startDestination)
            }
        }
    }
}