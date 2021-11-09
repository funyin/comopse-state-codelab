/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.state.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    currentlyEditing: TodoItem?,
    onEditItemChanged: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onStartEdit: (TodoItem) -> Unit
) {
    Column {
        val enableTopSection = currentlyEditing == null
        TodoItemInputBackground(elevate = enableTopSection, modifier = Modifier.fillMaxWidth()) {
            if (enableTopSection)
                TodoItemEntryInput(onItemComplete = onAddItem::invoke)
            else
                Text(
                    text = "Editing",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .padding(16.dp)
                )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) {
                if (currentlyEditing?.id == it.id)
                    TodoIemInlineEditor(
                        item = it,
                        onEditItemChanged = onEditItemChanged,
                        onEditItemDone = onEditDone,
                        onRemoveItem = onRemoveItem
                    )
                else
                    TodoRow(
                        todo = it,
                        onItemClicked = onStartEdit::invoke,
                        modifier = Modifier.fillParentMaxWidth()
                    )
            }
        }
        // For quick testing, a random item generator button
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("Add random item")
        }
    }
}

/**
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 */
@Composable
fun TodoRow(todo: TodoItem, onItemClicked: (TodoItem) -> Unit, modifier: Modifier = Modifier) {
    val iconAlpha by remember(key1 = todo.id) {
        mutableStateOf(randomTint())
    }
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task, modifier = Modifier.weight(1f))
        Icon(
            imageVector = todo.icon.imageVector,
            contentDescription = stringResource(id = todo.icon.contentDescription),
            tint = LocalContentColor.current.copy(alpha = iconAlpha)
        )
    }
}

@Composable
fun TodoIemInlineEditor(
    item: TodoItem,
    onEditItemChanged: (TodoItem) -> Unit,
    onEditItemDone: () -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) {
    TodoItemInput(
        text = item.task, setText = { onEditItemChanged(item.copy(task = it)) },
        icon = item.icon, setIcon = { onEditItemChanged(item.copy(icon = it)) },
        showIconsTray = true, submit = onEditItemDone
    ) {
        val shrinkModifier = Modifier.widthIn(20.dp)
        val textWidthModifier = Modifier.width(30.dp)
        Row {
            TextButton(
                onClick = onEditItemDone,
                modifier = shrinkModifier
            ) {
                Text(
                    text = "\uD83D\uDCBE",
                    textAlign = TextAlign.Center,
                    modifier = textWidthModifier
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = { onRemoveItem(item) },
                modifier = shrinkModifier
            ) {
                Text(text = "❌", textAlign = TextAlign.Center, modifier = textWidthModifier)
            }
        }
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, {}, {}, null, {}, {}) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview() = TodoItemEntryInput(onItemComplete = {})
