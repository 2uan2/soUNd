package com.example.sound.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onPerformSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) } // Sử dụng TextFieldValue
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tìm kiếm") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { newValue -> searchText = newValue },
                label = { Text("Tìm kiếm bài hát, nghệ sĩ...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") },
                trailingIcon = {
                    if (searchText.text.isNotEmpty()) {
                        IconButton(onClick = {
                            searchText = TextFieldValue("") // Xóa text khi click
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear search")
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onPerformSearch(searchText.text) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tìm kiếm")
            }
            // Thêm phần hiển thị kết quả tìm kiếm ở đây (ví dụ: LazyColumn)
            // For now, let's just show a placeholder
            Spacer(modifier = Modifier.height(16.dp))
            Text("Kết quả tìm kiếm sẽ hiển thị ở đây...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}