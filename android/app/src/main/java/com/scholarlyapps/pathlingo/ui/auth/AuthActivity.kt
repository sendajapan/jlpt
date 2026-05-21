package com.scholarlyapps.pathlingo.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity
import com.scholarlyapps.pathlingo.data.DataManager
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(this)

        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = Route.SPLASH) {
                    composable(Route.SPLASH) {
                        SplashScreen(
                            onAuthenticated = ::goToDashboard,
                            onNeedsLogin = {
                                nav.navigate(Route.LOGIN) {
                                    popUpTo(Route.SPLASH) { inclusive = true }
                                }
                            },
                        )
                    }
                    composable(Route.LOGIN) {
                        LoginScreen(
                            onAuthenticated = ::goToDashboard,
                            onNavigateSignup = { nav.navigate(Route.SIGNUP) },
                        )
                    }
                    composable(Route.SIGNUP) {
                        SignupScreen(
                            onAuthenticated = ::goToDashboard,
                            onNavigateLogin = {
                                nav.popBackStack(Route.LOGIN, inclusive = false)
                            },
                        )
                    }
                }
            }
        }
    }

    private fun goToDashboard() {
        DataManager.getInstance().loadData(applicationContext)
        startActivity(Intent(this, MainDashboardActivity::class.java))
        finish()
    }

    private object Route {
        const val SPLASH = "splash"
        const val LOGIN = "login"
        const val SIGNUP = "signup"
    }
}
