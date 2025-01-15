package com.example.firabasepam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firabasepam.model.Mahasiswa
import com.example.firabasepam.repository.MahasiswaRepository
import com.example.firabasepam.ui.navigation.DestinasiDetail
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class DetailUiState {
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val message: Throwable) : DetailUiState()
    object Loading : DetailUiState()
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mhs: MahasiswaRepository
) : ViewModel() {

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    private val _nim: String = checkNotNull(savedStateHandle[DestinasiDetail.NIM])

    init {
        getMahasiswabyNim()
    }

    fun getMahasiswabyNim() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            mhs.getMahasiswaByNim(_nim)
                .onStart {
                    detailUiState = DetailUiState.Loading
                }
                .catch {
                    detailUiState = DetailUiState.Error(it)
                }
                .collect { mahasiswa ->
                    detailUiState = DetailUiState.Success(mahasiswa)
                }
        }
    }
}