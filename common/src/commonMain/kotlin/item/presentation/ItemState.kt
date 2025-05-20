package item.presentation

import item.domain.model.Item

data class ItemState(val items: List<Item> = emptyList(), val isLoading: Boolean = false)
