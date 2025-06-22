package nodomain.pacjo.kittycode.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import nodomain.pacjo.kittycode.data.StatusCodeInfo
import nodomain.pacjo.kittycode.ui.screens.DetailsScreen
import nodomain.pacjo.kittycode.ui.screens.MainScreen
import nodomain.pacjo.kittycode.ui.theme.AppTheme

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

const val STATUS_CODE_IMAGE_SHARED_TRANSITION_KEY = "statusCodeImage"
const val MORE_INFO_SHARED_TRANSITION_KEY = "moreInfo"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun App() {
    val navController = rememberNavController()

    AppTheme {
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = MainScreen
            ) {
                composable<MainScreen> {
                    MainScreen(
                        navController,
                        this@SharedTransitionLayout,
                        this@composable,
                    )
                }

                composable<DetailsScreen>(
                    typeMap = mapOf(
                        typeOf<StatusCodeInfo>() to StatusCodeInfo.type
                    )
                ) { backStackEntry ->
                    val detailsScreen = backStackEntry.toRoute<DetailsScreen>()
                    DetailsScreen(
                        detailsScreen,
                        navController,
                        this@SharedTransitionLayout,
                        this@composable
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    App()
}