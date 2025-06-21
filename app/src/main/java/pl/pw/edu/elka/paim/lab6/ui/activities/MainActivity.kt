package pl.pw.edu.elka.paim.lab6.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import pl.pw.edu.elka.paim.lab6.data.StatusCodeInfo
import pl.pw.edu.elka.paim.lab6.ui.screens.DetailsScreen
import pl.pw.edu.elka.paim.lab6.ui.screens.MainScreen
import pl.pw.edu.elka.paim.lab6.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            App()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = MainScreen
        ) {
            composable<MainScreen> { MainScreen(navController) }

            composable<DetailsScreen>(
                typeMap = mapOf(
                    typeOf<StatusCodeInfo>() to StatusCodeInfo.type
                )
            ) { backStackEntry ->
                val detailsScreen = backStackEntry.toRoute<DetailsScreen>()
                DetailsScreen(detailsScreen, navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    App()
}
