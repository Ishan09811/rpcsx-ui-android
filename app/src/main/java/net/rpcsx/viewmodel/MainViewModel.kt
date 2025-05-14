
package net.rpcsx.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _isBottomNavigationVisible = MutableStateFlow(true)
    val isBottomNavigationVisible = _isBottomNavigationVisible.asStateFlow()

    fun setBottomNavigationVisiblity(visible: Boolean) {
        _isBottomNavigationVisible.value = visible
    }
}
