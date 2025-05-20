package item.presentation

sealed class ItemIntent {
    data object LoadItems : ItemIntent()
}

