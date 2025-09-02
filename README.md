# Keyboard Maestro TV

A beautiful Android TV app for controlling Keyboard Maestro scripts on your Mac, inspired by macOS design principles.

## Features

- **macOS-Inspired Design**: Beautiful, clean interface with macOS color palette and typography
- **TV-Optimized Navigation**: Full D-pad support with smooth focus animations
- **Script Management**: Discover and execute Keyboard Maestro scripts remotely
- **Connection Settings**: Easy configuration of Mac IP address and port
- **Real-time Feedback**: Toast notifications for script execution status

## Screenshots

The app features a clean, modern interface with:
- Grid layout of available scripts
- macOS-style cards with rounded corners and shadows
- Smooth focus animations for TV navigation
- Settings screen for connection configuration

## Prerequisites

### On your Mac:
1. **Keyboard Maestro** must be installed and running
2. **Web Server** must be enabled in Keyboard Maestro preferences
3. **Network access** must be allowed for the web server

### Keyboard Maestro Setup:
1. Open Keyboard Maestro
2. Go to Preferences → Web Server
3. Enable "Enable Web Server"
4. Note the port number (default is usually 1234)
5. Make sure "Allow remote access" is enabled

## Installation

### Build from Source:
```bash
git clone <repository-url>
cd KeyboardMaestroTV
./gradlew assembleDebug
```

### Install on Android TV:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. **Launch the app** from your Android TV launcher
2. **Configure connection** by going to Settings and entering your Mac's IP address and port
3. **Test connection** to ensure the app can reach your Mac
4. **Browse scripts** - the app will automatically discover available Keyboard Maestro scripts
5. **Execute scripts** by selecting them with your TV remote

## Configuration

### Connection Settings:
- **Mac IP Address**: Your Mac's local IP address (e.g., 192.168.1.100)
- **Mac Port**: The port Keyboard Maestro web server is running on (default: 1234)
- **Connection Timeout**: How long to wait for responses (default: 5000ms)

### Finding Your Mac's IP Address:
1. Open System Preferences → Network
2. Select your active connection (Wi-Fi or Ethernet)
3. Note the IP address shown

## Development

### Project Structure:
```
app/
├── src/main/
│   ├── java/com/chrislapointe/keyboardmaestrotv/
│   │   ├── MainActivity.kt              # Main app screen
│   │   ├── SettingsActivity.kt          # Settings screen
│   │   ├── models/                      # Data models
│   │   ├── network/                     # Network services
│   │   └── adapters/                    # RecyclerView adapters
│   ├── res/
│   │   ├── layout/                      # UI layouts
│   │   ├── drawable/                    # Icons and graphics
│   │   ├── values/                      # Strings, colors, themes
│   │   └── mipmap-*/                    # App icons
│   └── AndroidManifest.xml
├── build.gradle                         # App-level build config
└── proguard-rules.pro
```

### Key Components:
- **MainActivity**: Displays grid of scripts and handles execution
- **SettingsActivity**: Manages connection configuration
- **KeyboardMaestroApiService**: Handles network communication
- **ScriptsAdapter**: Manages script display in RecyclerView
- **ConnectionSettings**: Stores and validates connection parameters

## Troubleshooting

### Connection Issues:
- Verify Keyboard Maestro web server is enabled
- Check that your Mac and Android TV are on the same network
- Ensure firewall isn't blocking the connection
- Try using your Mac's IP address instead of localhost

### Script Execution Issues:
- Verify scripts are enabled in Keyboard Maestro
- Check Keyboard Maestro logs for errors
- Ensure scripts don't require user interaction

### App Issues:
- Clear app data and reconfigure connection
- Check Android TV has sufficient storage
- Restart the app if it becomes unresponsive

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test on Android TV
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by the official Keyboard Maestro iOS app
- Uses macOS design principles for a familiar experience
- Built with Android TV best practices for optimal TV navigation
