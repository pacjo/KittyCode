package pl.pw.edu.elka.paim.lab6.data

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusCodeInfo(
    val statusCode: Int,
    val title: String?,
    val description: String?,
    val imageUrl: String
) : Parcelable {
    companion object {
        const val INTENT_EXTRA = "status_code_info"

        fun getCodeInfoFromIntent(intent: Intent): StatusCodeInfo? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(INTENT_EXTRA, StatusCodeInfo::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<StatusCodeInfo>(INTENT_EXTRA)
            }
        }
    }
}