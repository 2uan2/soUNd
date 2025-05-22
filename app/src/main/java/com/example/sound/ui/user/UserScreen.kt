package com.example.sound.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LibraryScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(12.dp))
            CategoryTabs()
            Spacer(modifier = Modifier.height(12.dp))
            LibraryList(items = emptyList())
        }

    }
}
