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

import com.codelabs.state.util.generateRandomTodoItem
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class TodoViewModelTest {
    lateinit var viewModel: TodoViewModel

    @Before
    fun initValues() {
        viewModel = TodoViewModel()
    }

    @Test
    fun addTodoItem() {
        viewModel.apply {
            val item1 = generateRandomTodoItem()
            addItem(item1)
            assertThat(todoItems).isEqualTo(listOf(item1))
        }
    }

    @Test
    fun removeTodoItem() {
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.apply {
            addItem(item1)
            addItem(item2)

            removeItem(item = item1)
            assertThat(todoItems).isEqualTo(listOf(item2))
        }
    }
}
