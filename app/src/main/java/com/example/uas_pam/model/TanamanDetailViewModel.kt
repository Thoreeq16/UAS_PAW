package com.example.uas_pam.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas_pam.repositori.RepositoriTanaman
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TanamanDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriTanaman: RepositoriTanaman
) : ViewModel() {

    private val TanamanID: Int = checkNotNull(savedStateHandle[DetailDestination.TanamanIdArg])
    val uiState: StateFlow<ItemDetailUiState> =
        repositoriTanaman.getTanamanStream(TanamanID).filterNotNull().map {
            ItemDetailUiState(detailTanaman = it.toDetailTanaman())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemDetailUiState()
        )

    suspend fun deleteItem() {
        repositoriTanaman.deleteTanaman(uiState.value.detailTanaman.toTanaman())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
data class  ItemDetailUiState(
    val outOfStock: Boolean = true,
    val detailTanaman: DetailTanaman = DetailTanaman()
)