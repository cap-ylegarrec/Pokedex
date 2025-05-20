package item.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import item.domain.usecase.GetItemsUseCase

class ItemViewModel(
    private val getItemsUseCase: GetItemsUseCase
) {
    private val _state = MutableStateFlow(ItemState())
    val state: StateFlow<ItemState> = _state.asStateFlow()

    fun handleIntent(intent: ItemIntent) {
        when (intent) {
            is ItemIntent.LoadItems -> loadItems()
        }
    }

    private fun loadItems() {
        _state.value = ItemState(isLoading = true)
        val items = getItemsUseCase.execute()
        _state.value = ItemState(items = items, isLoading = false)
    }
}