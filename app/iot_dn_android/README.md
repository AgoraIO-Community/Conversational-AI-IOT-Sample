# BLE (Bluetooth Low Energy) Device Provisioning Library

## Project Overview

This is a Bluetooth Low Energy (BLE) device provisioning library for Android devices, primarily used to connect smart devices via BLE and configure their WiFi network connections. The project contains two main modules:

1. **lib module**: Core BLE functionality library providing BLE device scanning, connection, WiFi management, and data interaction capabilities
2. **app module**: Sample application demonstrating how to use the lib library for BLE device provisioning

## Features

### Core Library Features

- BLE device scanning and discovery
- BLE device connection management
- BLE data transmission
- Current connected WiFi information

### Sample Application Features

- Permission request interface
- WiFi information retrieval
- BLE device scanning
- Device list display
- Device connection and provisioning

## Technical Architecture

### Core Library Architecture

- **Callback Interfaces**: `BleConnectionCallback`, `BleScanCallback`, `BleListener`
- **Connectors**: `BleConnector`, `IBleConnector`
- **Scanners**: `BleScanner`, `IBleScanner`
- **Managers**: `BleManager`, `IBleManager`, `WifiManager`, `IWifiManager`
- **Data Models**: `BleDevice`, `WifiInfo`
- **State Management**: `BleConnectionState`, `BleScanState`
- **Utilities**: `BleUtils`, `BleLogger`

### Sample Application Architecture

- **MVVM Architecture**: Using ViewModel and StateFlow
- **Jetpack Compose UI**: Modern declarative UI
- **Permission Management**: `BlePermissionManager`
- **ANR Monitoring**: `AnrMonitor`

## Usage

### Add Dependency

```gradle
implementation project(":lib")
```

### Initialize BLE Logger

```kotlin
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
```

### Create BLE Manager

```kotlin
val bleManager = BleManager(context)
```

### Add Listener

```kotlin
bleManager.addListener(object : BleListener {
    override fun onScanStateChanged(state: BleScanState) {
        // Handle scan state changes
    }
    
    override fun onDeviceFound(device: BleDevice) {
        // Handle discovered devices
    }
    
    override fun onConnectionStateChanged(state: BleConnectionState) {
        // Handle connection state changes
    }
    
    override fun onDataReceived(uuid: String, data: ByteArray) {
        // Handle received data
    }
})
```

### Scan Devices

```kotlin
bleManager.startScan(null)  // Scan all devices without filter
```

### BLE Device Connection and Provisioning Flow

1. **Connect to BLE device**
```kotlin
bleManager.connect(device.device)
```

2. **Get Device ID**
```kotlin
val deviceId = bleManager.getDeviceId()
```

3. **Get Token**
Completed by business layer

4. **Scan WiFi List**
```kotlin
val wifiList = bleManager.queryWifiList()
// Returns WiFi list as JSON string, e.g.: ["HUAWEI-G108S1","NXIOT","RTM_2.4G",...]
```

5. **Configure WiFi Network**
```kotlin
bleManager.distributionNetwork(device.device, ssid, password, token, url)
```

6. **Disconnect**
```kotlin
bleManager.disconnect()
```

Please ensure operations are executed sequentially in a background thread as described above: first connect to the device, then get device ID, scan WiFi list, and finally perform WiFi provisioning and disconnect.

### BLE APN Flow

BLE APN (Access Point Name) mode allows devices to provide network access services through Bluetooth connection. This mode is typically used for device configuration or network diagnostics.

1. **Connect to BLE device**
```kotlin
bleManager.connect(device.device)
```

2. **Get Device ID**
```kotlin
val deviceId = bleManager.getDeviceId()
```

3. **Get Token**
Completed by business layer

4. **Set Device ID, Token, and URL**
```kotlin
// Send device ID
bleManager.sendSSID(deviceId)

// Send Token (sent in two parts)
bleManager.sendToken(token)

// Send URL
bleManager.sendUrl(url)
```

5. **Start BLE APN Mode**
```kotlin
val bluetoothName = bleManager.startBleAPN()
if (bluetoothName.isNotEmpty()) {
    // APN mode started successfully
} else {
    // APN mode failed to start
}
```

6. **Disconnect**
```kotlin
bleManager.disconnect()
```

Please ensure operations are executed sequentially in a background thread as described above: first connect to the device, then get device ID, set device ID, Token, and URL, and finally start APN mode and disconnect.



## Permission Requirements

The application requires the following permissions:

```xml
<!-- WiFi permissions -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Bluetooth permissions -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```


## Notes

1. Dynamic permission requests are required on Android 6.0 and above devices
2. For Android 12 and above devices, pay special attention to `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT` permissions
3. Location permission is required for BLE scanning
4. Ensure the target device supports BLE functionality
