package com.crypto.tranxact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.crypto.tranxact.data.RepositoryImpl
import com.crypto.tranxact.data.models.Asset
import com.crypto.tranxact.data.models.Exchange
import com.crypto.tranxact.domain.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: Repository) : ViewModel() {


    private val _assetStateFlow = MutableStateFlow(UIState<List<Asset>>())
    val assetStateFlow = _assetStateFlow.asStateFlow()

    private val _exchangeStateFlow = MutableStateFlow(UIState<List<Exchange>>())
    val exchangeStateFlow = _exchangeStateFlow.asStateFlow()

    fun getAssets() {
        viewModelScope.launch {
            _assetStateFlow.emit(UIState(isLoading = true))

            val res = repository.assets()

            res.bimap(
                {
                    _assetStateFlow.emit(UIState(isLoading = false, error = it))
                },
                {
                    _assetStateFlow.emit(UIState(isLoading = false, data = it))
                },
            )
        }
    }

    fun getExchanges() {
        viewModelScope.launch {
            _exchangeStateFlow.emit(UIState(isLoading = true))

            val res = repository.exchanges()

            res.bimap(
                {
                    _exchangeStateFlow.emit(UIState(isLoading = false, error = it))
                },
                {
                    _exchangeStateFlow.emit(UIState(isLoading = false, data = it))
                },
            )
        }
    }
}

class ViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return  MainViewModel(RepositoryImpl.instance) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

data class UIState<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: T? = null
)