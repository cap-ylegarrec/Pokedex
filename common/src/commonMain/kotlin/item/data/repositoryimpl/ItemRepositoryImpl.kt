package item.data.repositoryimpl

import item.data.datasource.MockItemDataSource
import item.domain.model.Item
import item.domain.repository.ItemRepository

class ItemRepositoryImpl(private val mockItemDataSource: MockItemDataSource) : ItemRepository {
    override fun getItems(): List<Item> {
        return mockItemDataSource.getItems()
    }
}