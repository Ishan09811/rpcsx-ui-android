
package net.rpcsx.ui.games

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil3.compose.AsyncImage
import net.rpcsx.GameRepository
import net.rpcsx.R
import net.rpcsx.RPCSXActivity
import net.rpcsx.utils.RTMDB
import net.rpcsx.viewmodel.GameDetailsViewModel

@Composable
fun GameDetailsScreen(
    gamePath: String,
    viewModel: GameDetailsViewModel = remember { GameDetailsViewModel(RTMDB()) }
) {
    LaunchedEffect(gamePath) {
        viewModel.load(Uri.decode(gamePath).substringAfterLast("/"))
    }

    val context = LocalContext.current
    val game = viewModel.game
    val loading = viewModel.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (game != null) {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(40.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
                    AsyncImage(
                        model = game.backgroundImage,
                        contentDescription = null,
                        onError = {
                            println("IMAGE ERROR: ${it.result.throwable}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black)
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                GameRepository.find(Uri.decode(gamePath))?.let { GameRepository.onBoot(it) }
                                val emulatorWindow = Intent(
                                    context, RPCSXActivity::class.java
                                )
                                emulatorWindow.putExtra("path", Uri.decode(gamePath))
                                context.startActivity(emulatorWindow)
                            },
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_play),
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    AsyncImage(
                        model = game.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(Modifier.width(16.dp))

                    Column {

                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        Text(
                            text = game.console,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(8.dp))

                        game.rating?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_star),
                                    contentDescription = null,
                                    tint = Color.Yellow
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "${it.score} (${it.total})",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = HtmlCompat.fromHtml(
                        game.description,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(20.dp))

                val screenshots = game.media?.images
                    ?.filter { it.type == "GAMEHUB_COVER_ART" }

                screenshots?.let {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(it) { img ->
                            AsyncImage(
                                model = img.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(Modifier.height(40.dp))
            }
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}