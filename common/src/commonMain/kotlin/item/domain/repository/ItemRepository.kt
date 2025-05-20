package item.domain.repository

import item.domain.model.Item

interface ItemRepository {
    fun getItems(): List<Item>
}