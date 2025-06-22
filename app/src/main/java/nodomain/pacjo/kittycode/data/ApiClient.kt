package nodomain.pacjo.kittycode.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

object ApiClient {
    const val TAG = "ApiClient"

    private val client: OkHttpClient = OkHttpClient()
    private const val URL = "https://http.cat"

    private fun makeRequest(endpoint: String): Response {
        assert(endpoint.startsWith('/')) {
            "Endpoint must start with '/'"
        }

        val request = Request.Builder()
            .url(URL + endpoint)
            .build()

        return client.newCall(request).execute()
    }

    private fun fetchStatusPageContent(httpCode: Int): String? {
        return try {
            val response = makeRequest("/status/$httpCode")
            if (response.isSuccessful) {
                response.body.string()
            } else {
                Log.e(TAG, "Failed to fetch status code $httpCode: ${response.code}")
                null
            }
        } catch (e: IOException) {
            Log.e(TAG, "IOException when fetching status code $httpCode", e)
            null
        }
    }

    private fun parseTitle(content: String?): String? {
        val pattern: Pattern = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL)
        val matcher: Matcher = pattern.matcher(content ?: "")

        return if (matcher.find()) {
            matcher.group(1)?.trim()?.removeSuffix(" | HTTP Cats")
        } else {
            null
        }
    }

    private fun parseDescription(content: String?): String? {
        val pattern: Pattern = Pattern.compile("<section class=\"flex justify-center tracking-wider\">(.*?)</section>", Pattern.DOTALL)
        val matcher: Matcher = pattern.matcher(content ?: "")

        return if (matcher.find()) {
            matcher.group(1)?.trim()
        } else {
            null
        }
    }

    fun getImageUrlForCode(httpCode: Int): String {
        return "$URL/$httpCode"
    }

    suspend fun getCodeDetails(httpCode: Int): StatusCodeInfo {
        val content = withContext(Dispatchers.IO) {
            fetchStatusPageContent(httpCode)
        }
        val title = parseTitle(content)
        val description = parseDescription(content)

        val data = StatusCodeInfo(
            statusCode = httpCode,
            title = title,
            description = description,
            imageUrl = getImageUrlForCode(httpCode)
        )

        return data
    }
}