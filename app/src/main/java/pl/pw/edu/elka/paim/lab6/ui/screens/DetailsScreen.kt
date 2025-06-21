package pl.pw.edu.elka.paim.lab6.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import pl.pw.edu.elka.paim.lab6.R
import pl.pw.edu.elka.paim.lab6.data.StatusCodeInfo
import pl.pw.edu.elka.paim.lab6.ui.activities.MORE_INFO_SHARED_TRANSITION_KEY
import pl.pw.edu.elka.paim.lab6.ui.activities.STATUS_CODE_IMAGE_SHARED_TRANSITION_KEY
import pl.pw.edu.elka.paim.lab6.ui.composables.StatusCodeImage
import pl.pw.edu.elka.paim.lab6.ui.utils.PreviewScope

@Serializable
data class DetailsScreen(val statusCodeInfo: StatusCodeInfo)

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun DetailsScreen(
    detailsScreen: DetailsScreen,
    navController: NavController,       // TODO: don't pass, callback from MainActivity instead
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val statusCodeInfo = detailsScreen.statusCodeInfo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(statusCodeInfo.title!!) },
                navigationIcon = {
                    val shape = MaterialShapes.Sunny.toShape()
                    IconButton(
                        onClick = { navController.navigateUp() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        shape = shape
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            with(sharedTransitionScope) {
                // TODO: would be nice to merge this with the one in MainScreen
                @Composable
                fun sharedElementModifier(key: String) = Modifier.sharedElement(
                    sharedTransitionScope.rememberSharedContentState(key),
                    animatedVisibilityScope = animatedContentScope
                )

                StatusCodeImage(
                    modifier = sharedElementModifier(STATUS_CODE_IMAGE_SHARED_TRANSITION_KEY),
                    statusCodeInfo = statusCodeInfo
                )

                Column(
                    modifier = sharedElementModifier(MORE_INFO_SHARED_TRANSITION_KEY)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(8.dp),
                ) {
                    Text(
                        text = AnnotatedString.fromHtml(statusCodeInfo.description!!),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {
    val statusCodeInfo = StatusCodeInfo(
        statusCode = 418,
        title = "418 - I'm a teapot",
        description = "The HTTP 418 I'm a teapot client error response code indicates that the server refuses to brew coffee because it is, permanently, a teapot.\n\n" +
                "A combined coffee/tea pot that is temporarily out of coffee should instead return 503. This error is a reference to Hyper Text Coffee Pot Control Protocol defined in April Fools' jokes in 1998 and 2014.\n\n" +
                "Some websites use this response for requests they do not wish to handle, such as automated queries.",
        imageUrl = "https://http.cat/images/418.jpg"
    )

    PreviewScope { sharedTransitionScope, animatedContentScope ->
        DetailsScreen(
            DetailsScreen(statusCodeInfo),
            rememberNavController(),
            sharedTransitionScope,
            animatedContentScope
        )
    }
}