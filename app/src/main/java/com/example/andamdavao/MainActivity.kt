package com.example.andamdavao

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.andamdavao.ui.screens.MainScreen
import com.example.andamdavao.ui.screens.SplashScreen
import com.example.andamdavao.ui.screens.WelcomeScreen
import com.example.andamdavao.ui.theme.AndamDavaoTheme

sealed class Screen {
    object Splash : Screen()
    object Welcome : Screen()
    object Main : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndamDavaoTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

                when (currentScreen) {
                    Screen.Splash -> {
                        SplashScreen(
                            onSplashComplete = {
                                currentScreen = Screen.Welcome
                            }
                        )
                    }

                    Screen.Welcome -> {
                        WelcomeScreen(
                            onContinueAsGuest = {
                                currentScreen = Screen.Main
                            },
                            onSignInClicked = {
                                Toast.makeText(
                                    this,
                                    "Firebase Sign In entry point. Entering as guest for now.",
                                    Toast.LENGTH_LONG
                                ).show()
                                currentScreen = Screen.Main
                            }
                        )
                    }

                    Screen.Main -> {
                        MainScreen(
                            onSignInClicked = {
                                Toast.makeText(
                                    this,
                                    "Firebase Sign In entry point.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}
