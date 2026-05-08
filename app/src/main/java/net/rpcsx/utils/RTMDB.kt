
package net.rpcsx.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
data class GameDetails(
    val titleId: String,
    val name: String,
    val icon: String,
    val description: String,
    val backgroundImage: String,
    val console: String,
    val media: Media?,
    val rating: Rating?
)

@Serializable
data class Media(
    val images: List<MediaImage> = emptyList()
)

@Serializable
data class MediaImage(
    val url: String,
    val type: String
)

@Serializable
data class Rating(
    val total: Int,
    val score: Double
)

class RTMDB {

    private val client = OkHttpClient()

    suspend fun getGameDetails(gameId: String): GameDetails? =
        withContext(Dispatchers.IO) {

            val url = "https://gamesdb.up.railway.app/api/Games/" +
                    "$gameId?lang=en-US"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            try {
                client.newCall(request).execute().use { response ->

                    if (!response.isSuccessful) return@withContext null

                    val body = response.body.string()

                    val json = Json { ignoreUnknownKeys = true }

                    json.decodeFromString<GameDetails>(body)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}