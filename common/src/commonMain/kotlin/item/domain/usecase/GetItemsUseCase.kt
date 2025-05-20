package item.domain.usecase

import item.domain.model.Item
import item.domain.repository.ItemRepository

class GetItemsUseCase(
    private val repository: ItemRepository
) {
    fun execute(): List<Item> {
        return repository.getItems()
    }
}
