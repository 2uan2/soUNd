package com.example.sound.ui.user

import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ListItem
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToAccountSettings: () -> Unit,
    onDarkModeClicked: () -> Unit,
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cài đặt", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                SettingsCategoryHeader("Tài khoản")
            }
            item {
                SettingsListItem(
                    icon = Icons.Default.AccountCircle,
                    text = "Quản lý hồ sơ",
                    onClick = onNavigateToAccountSettings
                )
            }
            item {
                SettingsListItem(
                    icon = Icons.Outlined.Star,
                    text = "Thông tin Premium",
                    onClick = { }
                )
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            item {
                SettingsCategoryHeader("Chung")
            }
            item {
                SettingsListItem(
                    icon = Icons.Default.Translate,
                    text = "Ngôn ngữ",
                    onClick = { }
                )
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            item {
                SettingsCategoryHeader("Phát nhạc")
            }
            item {
                SettingsListItem(
                    icon = Icons.Default.HighQuality,
                    text = "Chất lượng âm thanh",
                    onClick = { }
                )
            }
            item {
                SettingsToggleItem(
                    icon = Icons.Default.Timer,
                    text = "Bộ hẹn giờ ngủ",
                    checked = false,
                    onCheckedChange = { }
                )
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            item {
                SettingsCategoryHeader("Giới thiệu & Hỗ trợ")
            }
            item {
                SettingsListItem(
                    icon = Icons.Default.Info,
                    text = "Phiên bản ứng dụng",
                    trailingContent = { Text("1.0.0") },
                    onClick = { }
                )
            }
            item {
                SettingsListItem(
                    icon = Icons.Default.Help,
                    text = "Trung tâm trợ giúp",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun SettingsCategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsListItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        headlineContent = {
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = trailingContent ?: {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        headlineContent = {
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    )
}