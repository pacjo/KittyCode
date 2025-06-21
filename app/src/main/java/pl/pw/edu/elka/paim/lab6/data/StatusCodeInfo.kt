package pl.pw.edu.elka.paim.lab6.data

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class StatusCodeInfo(
    val statusCode: Int,
    val title: String?,
    val description: String?,
    val imageUrl: String
) : Parcelable {
    companion object {
        val type = object : NavType<StatusCodeInfo>(
            isNullableAllowed = false
        ) {
            override fun put(bundle: SavedState, key: String, value: StatusCodeInfo) {
                bundle.putParcelable(key, value)
            }

            override fun get(bundle: SavedState, key: String): StatusCodeInfo? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable<StatusCodeInfo>(key, StatusCodeInfo::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    bundle.getParcelable<StatusCodeInfo>(key)
                }
            }

            override fun parseValue(value: String): StatusCodeInfo {
                return Json.decodeFromString<StatusCodeInfo>(value)
            }

            override fun serializeAsValue(value: StatusCodeInfo): String {
                return Uri.encode(Json.encodeToString(value))
            }
        }
    }
}