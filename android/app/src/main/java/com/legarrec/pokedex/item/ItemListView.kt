package com.legarrec.pokedex.item

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import item.data.datasource.MockItemDataSource
import item.data.repositoryimpl.ItemRepositoryImpl
import item.domain.usecase.GetItemsUseCase
import item.presentation.ItemIntent
import item.presentation.ItemViewModel

@Composable
fun ItemListView() {
    val viewModel = ItemViewModel(GetItemsUseCase(ItemRepositoryImpl(MockItemDataSource())))
    val state by viewModel.state.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.items) { item ->
            ItemView(item = item)
        }
    }

    // Trigger loading items
    viewModel.handleIntent(ItemIntent.LoadItems)
}

@Preview(showBackground = true)
@Composable
fun PreviewItemListView() {
    ItemListView()
}