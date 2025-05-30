package pl.pw.edu.elka.paim.lab6.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.pw.edu.elka.paim.lab6.R
import pl.pw.edu.elka.paim.lab6.data.StatusCodeInfo
import pl.pw.edu.elka.paim.lab6.ui.composables.StatusCodeImage
import pl.pw.edu.elka.paim.lab6.ui.theme.AppTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val codeInfo = StatusCodeInfo.getCodeInfoFromIntent(intent)

        setContent {
            val context = LocalContext.current

            // quit on fail
            if (codeInfo == null) {
                Toast.makeText(
                    context,
                    R.string.message_failed_retrieving_item,
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
                return@setContent       // to allow for codeInfo smart-cast
            }

            AppTheme {
                DetailsScreen(codeInfo)
            }
        }
    }

    companion object {
        const val TAG = "DetailsActivity"
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DetailsScreen(statusCodeInfo: StatusCodeInfo) {
    val activity = LocalActivity.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(statusCodeInfo.title!!) },
                navigationIcon = {
                    val shape = MaterialShapes.Sunny.toShape()
                    IconButton(
                        onClick = {
                            // not-null assertion here since crashing is closer to going back
                            // then not doing anything
                            activity!!.finish()
                        },
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
            StatusCodeImage(statusCodeInfo)

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(8.dp)
            ) {
                Text(
                    text = AnnotatedString.fromHtml(statusCodeInfo.description!!),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

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

    AppTheme {
        DetailsScreen(statusCodeInfo)
    }
}