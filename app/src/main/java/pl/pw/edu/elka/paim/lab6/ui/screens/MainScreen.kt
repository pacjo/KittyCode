package pl.pw.edu.elka.paim.lab6.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
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
import pl.pw.edu.elka.paim.lab6.R
import pl.pw.edu.elka.paim.lab6.data.ApiClient.getCodeDetails
import pl.pw.edu.elka.paim.lab6.data.StatusCodeInfo
import pl.pw.edu.elka.paim.lab6.ui.composables.BackgroundImage
import pl.pw.edu.elka.paim.lab6.ui.composables.CodeSearchBar
import pl.pw.edu.elka.paim.lab6.ui.composables.FullPageLoadingIndicator
import pl.pw.edu.elka.paim.lab6.ui.composables.StatusCodeImage
import pl.pw.edu.elka.paim.lab6.ui.theme.AppTheme

@Serializable
object MainScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var queryHistory = remember { mutableStateListOf<String>() }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }
    var codeInfo by remember { mutableStateOf<StatusCodeInfo?>(null) }

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
                    StatusCodeImage(codeInfo)

                    Spacer(Modifier.height(16.dp))

                    FilledTonalButton(
                        onClick = { navController.navigate(DetailsScreen(codeInfo)) },
                        shape = squareShape
                    ) {
                        Text(stringResource(R.string.see_more))
                    }
                }
            }

            if (loading) {
                FullPageLoadingIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    AppTheme {
        MainScreen(rememberNavController())
    }
}
