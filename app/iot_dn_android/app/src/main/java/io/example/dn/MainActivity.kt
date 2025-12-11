package io.example.dn

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.example.dn.model.DeviceConnectConfig
import io.example.dn.model.WifiConfig
import io.example.dn.permission.BlePermissionCallback
import io.example.dn.permission.BlePermissionManager
import io.example.dn.ui.theme.AndroidTheme
import io.example.dn.viewmodel.BleViewModel
import io.iot.dn.ble.log.BleLogCallback
import io.iot.dn.ble.log.BleLogLevel
import io.iot.dn.ble.log.BleLogger
import io.iot.dn.ble.model.BleDevice

class MainActivity : ComponentActivity() {
    private lateinit var permissionManager: BlePermissionManager
    private val bleViewModel: BleViewModel by viewModels()
    private val anrMonitor = AnrMonitor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = BlePermissionManager(this)

        BleLogger.init(object : BleLogCallback {
            override fun onLog(level: BleLogLevel, tag: String, message: String) {
                when (level) {
                    BleLogLevel.DEBUG -> Log.d(tag, message)
                    BleLogLevel.INFO -> Log.i(tag, message)
                    BleLogLevel.WARN -> Log.w(tag, message)
                    BleLogLevel.ERROR -> Log.e(tag, message)
                }
            }
        })

        setContent {
            AndroidTheme {
                BleScreen()
            }
        }

        permissionManager.setCallback(object : BlePermissionCallback {
            override fun onPermissionsGranted() {
                // Permissions granted, can start scanning

            }

            override fun onPermissionsDenied() {
                // Show error message
                showError("Bluetooth and location permissions are required to use this feature")
            }
        })

        anrMonitor.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        anrMonitor.stop()
    }

    private fun showError(message: String) {
        // Display error message, can use Toast or Dialog
        setContent {
            AndroidTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = message)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getPermissionManager(): BlePermissionManager {
        return permissionManager
    }
}

@Composable
fun BleScreen(viewModel: BleViewModel = viewModel()) {
    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val wifiInfo by viewModel.wifiInfo.collectAsStateWithLifecycle()
    val wifiPassword by viewModel.wifiPassword.collectAsStateWithLifecycle()
    val wifiList by viewModel.wifiList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Request permissions button
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // WiFi permission request button
            Button(
                onClick = {
                    (context as? MainActivity)?.getPermissionManager()?.checkAndRequestPermissions()
                }, modifier = Modifier.weight(1f)
            ) {
                Text("Request Permissions")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Check WiFi permissions button
        Button(
            onClick = {
                val hasWifiPermissions = viewModel.checkWiFiPermission()
                Toast.makeText(
                    context, if (hasWifiPermissions) "WiFi permission granted" else "WiFi permission not granted", Toast.LENGTH_SHORT
                ).show()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Check WiFi Permissions")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Get WiFi list button
        Button(
            onClick = {
                viewModel.getWifiList()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get WiFi List")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // WiFi list display
        if (wifiList.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "WiFi List (${wifiList.size} networks)",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(wifiList) { ssid ->
                            WifiItem(
                                ssid = ssid,
                                isSelected = wifiInfo == ssid,
                                onClick = {
                                    viewModel.setWifiInfo(ssid)
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Get current WiFi info button
        Button(
            onClick = {
                viewModel.getCurrentWifiInfo()
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Current WiFi Info")
        }

        // Display WiFi info
        if (wifiInfo != "Unknown") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = wifiInfo, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // WiFi password input field
        OutlinedTextField(
            value = wifiPassword,
            onValueChange = { viewModel.setWifiPassword(it) },
            label = { Text("WiFi Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Scan button
        Button(
            onClick = {
                val missingPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    listOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ).any { permission ->
                        ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                    }
                } else {
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) != PackageManager.PERMISSION_GRANTED
                }

                if (missingPermissions) {
                    (context as? MainActivity)?.getPermissionManager()?.checkAndRequestPermissions()
                    return@Button
                }

                if (isScanning) viewModel.stopScan()
                else viewModel.startScan()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isScanning) "Stop Scan" else "Start Scan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Message notification
        if (message.isNotEmpty()) {
            Text(
                text = message, modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Device list
        if (devices.isNotEmpty()) {
            Text(
                text = "Discovered Devices (${devices.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            devices.forEach { device ->
                DeviceCard(
                    device = device,
                    onConnect = { viewModel.connect(device) },
                    onGetDeviceId = { viewModel.getDeviceId() },
                    onDisconnect = { viewModel.disconnect() },
                    ssid = wifiInfo,
                    password = wifiPassword,
                    onActivate = { config ->
                        // Network configuration logic can be added here
                        viewModel.configureDevice(
                            config.wifiConfig.ssid,
                            config.wifiConfig.pwd ?: "",
                            "xxxx.xxxxx.xxxx",
                            "http://10.91.0.63:5001",
                        )
                    },
                    onBleApn = { viewModel.bleApn() }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    device: BleDevice,
    onConnect: (() -> Unit)? = null,
    onGetDeviceId: (() -> Unit)? = null,
    onDisconnect: (() -> Unit)? = null,
    ssid: String? = null,
    password: String? = null,
    onActivate: ((DeviceConnectConfig) -> Unit)? = null,
    onBleApn: (() -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = { }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Device basic information
            Text(
                text = device.name, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "MAC: ${device.address}", style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "RSSI: ${device.rssi}", style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Connect button
            Button(
                onClick = {
                    onConnect?.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Get device ID button
            Button(
                onClick = {
                    onGetDeviceId?.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Device ID")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Configure network button
            Button(
                onClick = {
                    val config = DeviceConnectConfig(
                        wifiConfig = WifiConfig(ssid = ssid ?: "", pwd = password ?: "")
                    )
                    onActivate?.invoke(config)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configure Network")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onBleApn?.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("BLE APN")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Disconnect button
            Button(
                onClick = {
                    onDisconnect?.invoke()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Disconnect")
            }
        }
    }
}

@Composable
fun WifiItem(
    ssid: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ssid,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}