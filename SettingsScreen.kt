package com.example.uinewdawn.ui.theme.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourapp.ui.theme.NewDawnGreen
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.yourapp.data.local.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToChangePassword: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    themeViewModel: ThemeViewModel = viewModel()
) {
    val themeMode by themeViewModel.themeMode.collectAsState()
    val scope = rememberCoroutineScope()

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SETTINGS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ---------- APPEARANCE ----------
            SectionHeader("Appearance")

            SettingsItem(
                icon = Icons.Default.Brightness6,
                title = "Theme",
                subtitle = when (themeMode) {
                    "light" -> "Light"
                    "dark" -> "Dark"
                    else -> "System default"
                },
                onClick = { showThemeDialog = true }
            )

            Spacer(Modifier.height(16.dp))

            // ---------- ACCOUNT ----------
            SectionHeader("Account")

            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Change Password",
                subtitle = "Update your login password",
                onClick = onNavigateToChangePassword
            )

            SettingsItem(
                icon = Icons.Default.Email,
                title = "Email Address",
                subtitle = "sarah@example.com",
                onClick = { /* TODO: Edit email */ }
            )

            Spacer(Modifier.height(16.dp))

            // ---------- NOTIFICATIONS ----------
            SectionHeader("Notifications")

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notification Preferences",
                subtitle = "Jobs, offers, and messages",
                onClick = onNavigateToNotifications
            )

            Spacer(Modifier.height(16.dp))

            // ---------- ABOUT ----------
            SectionHeader("About")

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About NewDawn",
                subtitle = "Version 1.0.0",
                onClick = { /* TODO */ }
            )

            SettingsItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Policy",
                subtitle = "How we handle your data",
                onClick = { /* TODO */ }
            )

            SettingsItem(
                icon = Icons.Default.HelpOutline,
                title = "Help & Support",
                subtitle = "Get in touch with us",
                onClick = { /* TODO */ }
            )

            Spacer(Modifier.height(24.dp))

            // ---------- LOGOUT ----------
            Button(
                onClick = {
                    scope.launch {
                        TokenManager.clear()
                        onLogout()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                )
            ) {
                Text("LOG OUT", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // ---------- THEME PICKER DIALOG ----------
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
            text = {
                Column {
                    ThemeOption("system", "Follow system", themeMode) {
                        themeViewModel.setThemeMode("system")
                        showThemeDialog = false
                    }
                    ThemeOption("light", "Light", themeMode) {
                        themeViewModel.setThemeMode("light")
                        showThemeDialog = false
                    }
                    ThemeOption("dark", "Dark", themeMode) {
                        themeViewModel.setThemeMode("dark")
                        showThemeDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }
}

// ---------- SECTION HEADER ----------
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 24.dp, top = 20.dp, bottom = 8.dp)
    )
}

// ---------- SETTINGS ITEM ----------
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = NewDawnGreen,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ---------- THEME OPTION ROW ----------
@Composable
fun ThemeOption(
    value: String,
    label: String,
    selected: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = NewDawnGreen)
        )
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 15.sp)
    }
}