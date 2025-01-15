package com.example.firabasepam.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firabasepam.model.Mahasiswa
import com.example.firabasepam.ui.viewmodel.DetailUiState
import com.example.firabasepam.ui.viewmodel.DetailViewModel
import com.example.firabasepam.ui.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detail")
                },
                navigationIcon = {
                    Button(onClick = navigateBack) {
                        Text("Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        DetailStatus(
            modifier = Modifier.padding(innerPadding),
            detailUiState = viewModel.detailUiState,
            retryAction = { viewModel.getMahasiswabyNim() }
        )
    }
}

@Composable
fun DetailStatus(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState
) {
    when (detailUiState) {
        is DetailUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is DetailUiState.Success -> {
            if (detailUiState.mahasiswa.nim.isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Data tidak ditemukan.")
                }
            } else {
                ItemDetailMhs(
                    mahasiswa = detailUiState.mahasiswa,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }

        is DetailUiState.Error -> OnError(message = detailUiState.message.message?:"Error", retryAction = retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun ItemDetailMhs(
    modifier: Modifier = Modifier,
    mahasiswa: Mahasiswa
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DetailMhs(judul = "NIM", isinya = mahasiswa.nim)
            DetailMhs(judul = "Nama", isinya = mahasiswa.nama)
            DetailMhs(judul = "Alamat", isinya = mahasiswa.alamat)
            DetailMhs(judul = "Jenis Kelamin", isinya = mahasiswa.jenis_kelamin)
            DetailMhs(judul = "Kelas", isinya = mahasiswa.kelas)
            DetailMhs(judul = "Angkatan", isinya = mahasiswa.angkatan)
            DetailMhs(judul = "Judul Skripsi", isinya = mahasiswa.judulSkripsi)
            DetailMhs(judul = "Dosen Pembimbing 1", isinya = mahasiswa.dosPem1)
            DetailMhs(judul = "Dosen Pembimbing 2", isinya = mahasiswa.dosPem2)
        }
    }
}

@Composable
fun DetailMhs(
    modifier: Modifier =Modifier,
    judul: String,
    isinya: String,
){
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start)
    {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}