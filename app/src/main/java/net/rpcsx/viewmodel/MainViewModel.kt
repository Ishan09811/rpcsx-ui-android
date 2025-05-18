
package net.rpcsx.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _isBottomNavigationVisible = MutableStateFlow(true)
    val isBottomNavigationVisible = _isBottomNavigationVisible.asStateFlow()

    private val _itemUsageMap = mutableStateMapOf<String, Long>()
    val itemUsageMap: Map<String, Long> get() = _itemUsageMap    

    fun setBottomNavigationVisibility(visible: Boolean) {
        _isBottomNavigationVisible.value = visible
    }

    fun recordItemUsage(key: String) {
        _itemUsageMap[key] = System.currentTimeMillis()
    }
}
