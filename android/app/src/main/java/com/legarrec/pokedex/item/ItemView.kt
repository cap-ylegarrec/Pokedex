package com.legarrec.pokedex.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import item.domain.model.Item

@Composable
fun ItemView(item: Item) {
    Text(
        text = item.name,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemView() {
    ItemView(item = Item(id= 1, name = "Pikachu"))
}