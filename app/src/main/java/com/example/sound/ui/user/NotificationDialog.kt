package com.example.sound.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDialog(
    notifications: List<String>, // Danh sách các thông báo
    onDismiss: () -> Unit,     // Callback khi đóng dialog
    onClearAll: () -> Unit     // Callback khi xóa tất cả thông báo
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Đóng dialog khi click ra ngoài hoặc nhấn nút back
        title = { Text("Thông báo") },
        text = {
            if (notifications.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, // Icon thông báo trống
                        contentDescription = "Không có thông báo",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Bạn không có thông báo nào.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(notifications) { notification ->
                        ListItem(
                            headlineContent = { Text(notification) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Notifications, // Icon cho từng thông báo
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (notifications.isNotEmpty()) {
                TextButton(onClick = onClearAll) {
                    Text("Xóa tất cả")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}