package item.data.datasource

import item.domain.model.Item

class MockItemDataSource {
    fun getItems(): List<Item> {
        return listOf(
            Item(1, "Item A"),
            Item(2, "Item B"),
            Item(3, "Item C")
        )
    }
}