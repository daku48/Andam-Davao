package com.example.andamdavao

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.andamdavao.ui.screens.MainScreen
import com.example.andamdavao.ui.screens.SplashScreen
import com.example.andamdavao.ui.screens.WelcomeScreen
import com.example.andamdavao.ui.theme.AndamDavaoTheme

enum class Screen { Splash, Welcome, Main }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("andam_prefs", Context.MODE_PRIVATE)

        setContent {
            AndamDavaoTheme {
                // rememberSaveable so rotation does not send the user back to the splash screen.
                var currentScreen by rememberSaveable { mutableStateOf(Screen.Splash) }

                when (currentScreen) {
                    Screen.Splash -> {
                        SplashScreen(
                            onSplashComplete = {
                                // Returning users go straight to the app. In an emergency
                                // nobody wants to tap through a welcome screen.
                                currentScreen = if (prefs.getBoolean(KEY_SEEN_WELCOME, false)) {
                                    Screen.Main
                                } else {
                                    Screen.Welcome
                                }
                            }
                        )
                    }

                    Screen.Welcome -> {
                        WelcomeScreen(
                            onContinueAsGuest = {
                                prefs.edit().putBoolean(KEY_SEEN_WELCOME, true).apply()
                                currentScreen = Screen.Main
                            },
                            onSignInClicked = {
                                Toast.makeText(
                                    this,
                                    "Sign in is coming soon. Continuing as guest.",
                                    Toast.LENGTH_LONG
                                ).show()
                                prefs.edit().putBoolean(KEY_SEEN_WELCOME, true).apply()
                                currentScreen = Screen.Main
                            }
                        )
                    }

                    Screen.Main -> {
                        MainScreen(
                            onSignInClicked = {
                                Toast.makeText(
                                    this,
                                    "Sign in is coming soon.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val KEY_SEEN_WELCOME = "seen_welcome"
    }
}
