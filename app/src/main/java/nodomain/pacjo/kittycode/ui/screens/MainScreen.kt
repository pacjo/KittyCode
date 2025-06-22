package nodomain.pacjo.kittycode.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults.squareShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import nodomain.pacjo.kittycode.R
import nodomain.pacjo.kittycode.data.ApiClient.getCodeDetails
import nodomain.pacjo.kittycode.data.StatusCodeInfo
import nodomain.pacjo.kittycode.ui.composables.BackgroundImage
import nodomain.pacjo.kittycode.ui.composables.CodeSearchBar
import nodomain.pacjo.kittycode.ui.composables.FullPageLoadingIndicator
import nodomain.pacjo.kittycode.ui.composables.StatusCodeImage
import nodomain.pacjo.kittycode.ui.utils.PreviewScope
import nodomain.pacjo.kittycode.data.StorageManager
import nodomain.pacjo.kittycode.ui.activities.MORE_INFO_SHARED_TRANSITION_KEY
import nodomain.pacjo.kittycode.ui.activities.STATUS_CODE_IMAGE_SHARED_TRANSITION_KEY

@Serializable
object MainScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // TODO: check - https://developer.android.com/develop/ui/compose/animation/shared-elements
//    LocalSharedTransitionScope.current
//    LocalNavAnimatedVisibilityScope.current

    var query by remember { mutableStateOf("") }
    var queryHistory = remember { mutableStateListOf<String>() }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }
    var codeInfo by rememberSaveable { mutableStateOf<StatusCodeInfo?>(null) }

    fun queryApi(query: String) {
        try {
            val httpCode = query.toInt()

            queryHistory.apply {
                // remove previous and add at the end
                remove(query)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    addFirst(query)
                } else {
                    add(0, query)
                }
            }
            expanded = false
            loading = true
            coroutineScope.launch {
                StorageManager.saveSearchHistory(context, queryHistory.toList())
                codeInfo = getCodeDetails(httpCode)

                // in case we get broken data let's not show anything
                if (codeInfo?.title == null || codeInfo?.description == null) {
                    codeInfo = null
                    Toast.makeText(
                        context,
                        context.getString(R.string.message_failed_retrieving_code_info),
                        Toast.LENGTH_LONG
                    ).show()
                }

                loading = false
            }
        } catch (_: NumberFormatException) {
            Toast.makeText(
                context,
                context.getString(R.string.message_failed_number_parsing, query),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        // add history from storage
        queryHistory.addAll(StorageManager.getSearchHistory(context))
    }

    // we want the expanded searchbar background to go under the system bars, so let's ignore innerPadding
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { _ ->
        if (codeInfo == null && !loading) {
            BackgroundImage(stringResource(R.string.search_to_begin))
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CodeSearchBar(
                expanded = expanded,
                query = query,
                onQueryChange = { query = it },
                onSearch = { queryApi(it) },
                searchResults = queryHistory.distinct(),
                onResultClick = {
                    query = it
                    queryApi(it)
                },
                onExpandedChange = { expanded = it }
            )

            // show image conditionally
            codeInfo?.let { codeInfo ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    with(sharedTransitionScope) {
                        // TODO: would be nice to merge this with the one in DetailsScreen
                        @Composable
                        fun sharedElementModifier(key: String) = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key),
                            animatedVisibilityScope = animatedContentScope
                        )

                        StatusCodeImage(
                            modifier = sharedElementModifier(STATUS_CODE_IMAGE_SHARED_TRANSITION_KEY),
                            statusCodeInfo = codeInfo
                        )

                        Spacer(Modifier.height(16.dp))

                        FilledTonalButton(
                            onClick = { navController.navigate(DetailsScreen(codeInfo)) },
                            modifier = sharedElementModifier(MORE_INFO_SHARED_TRANSITION_KEY),
                            shape = squareShape
                        ) {
                            Text(stringResource(R.string.see_more))
                        }
                    }
                }
            }

            if (loading) {
                FullPageLoadingIndicator()
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    PreviewScope { sharedTransitionScope, animatedContentScope ->
        MainScreen(
            rememberNavController(),
            sharedTransitionScope,
            animatedContentScope
        )
    }
}