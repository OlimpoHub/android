package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import android.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.common.components.ErrorView
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components.SupplyDetailContent
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliesDetailScreen(
    idInsumo: String,
    onBackClick: () -> Unit,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    modifySupplyBatch: () -> Unit,
    deleteSupplyBatch: () -> Unit,
    viewModel: SuppliesDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(idInsumo) {
        viewModel.getSupply(idInsumo)
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            uiState
                                .supplyBatchList
                                ?.name ?: "Insumo",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = White,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    LoadingShimmer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                    )
                }
                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Unknown Error",
                        onRetry = { viewModel.getSupply(idInsumo) },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                uiState.supplyBatchList != null -> {
                    SupplyDetailContent(
                        supply = uiState.supplyBatchList!!,
                        onClickAddSupplyBatch = onClickAddSupplyBatch,
                        onClickDelete = onClickDelete,
                        onClickModify = onClickModify,
                        modifySupplyBatch = modifySupplyBatch,
                        deleteSupplyBatch = deleteSupplyBatch,
                    )
                }
            }
        }
    }
}
