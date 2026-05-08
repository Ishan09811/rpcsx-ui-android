
package net.rpcsx.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.rpcsx.utils.GameDetails
import net.rpcsx.utils.RTMDB

class GameDetailsViewModel(
    private val api: RTMDB
) : ViewModel() {

    var game by mutableStateOf<GameDetails?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun load(gameId: String) {
        viewModelScope.launch {
            isLoading = true
            game = api.getGameDetails(gameId)
            Log.i("GameDetailsViewModel", "Loaded: $gameId backgroundImage: ${game?.backgroundImage} ")
            isLoading = false
        }
    }
}