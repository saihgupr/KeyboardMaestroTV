# Keyboard Maestro TV

A beautiful Android TV app for controlling Keyboard Maestro scripts on your Mac with macOS-inspired design. Primarily designed for Android TV, but also works on mobile devices with automatic orientation support.

## Screenshots

<div align="center">
  <img src="https://i.imgur.com/vsbRlBJ.png" width="50%" alt="Main Screen" />
  <img src="https://i.imgur.com/Fw6OocR.png" width="50%" alt="Settings Screen" />
</div>

*Main screen showing the script grid with shortcut buttons | Settings screen for configuring Mac connection*

## Features

- **macOS Design**: Clean interface with macOS colors and typography
- **TV-First Design**: Optimized for Android TV with D-pad navigation
- **TV Navigation**: Full D-pad support with focus animations
- **Remote Control**: Execute Keyboard Maestro scripts from your TV
- **Grid Layout**: Configurable columns for script organization
- **Mobile Compatible**: Also works on phones and tablets (not the primary use case)

## Setup

### On your Mac:
1. Install and run **Keyboard Maestro**
2. Enable **Web Server** in Preferences → Web Server
3. Note the port (default: 4490)
4. Allow remote access

### Script Configuration:
For scripts to appear in the app, they must have the **"Public Web" trigger** enabled:
1. Open the script in Keyboard Maestro
2. Add a new trigger: **"Public Web"**
3. The script will now be accessible via the web interface
4. Scripts without this trigger will not appear in the app

### On Android TV:

**Option 1: Download Pre-built APK (Recommended)**
- Download the latest APK from [Releases](https://github.com/saihgupr/KeyboardMaestroTV/releases)
- Install via ADB: `adb install KeyboardMaestroTV-v1.0.0.apk`

**Option 2: Build from Source**
```bash
git clone https://github.com/saihgupr/KeyboardMaestroTV
cd KeyboardMaestroTV
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. Launch the app on your Android TV
2. Configure your Mac's IP address and port in Settings
3. Test the connection
4. Browse and execute scripts with your TV remote

*Note: The app also works on mobile devices and tablets, but it's primarily designed for Android TV.*

## Configuration

- **IP Address**: Your Mac's local network address
- **Port**: Keyboard Maestro web server port (default: 1234)
- **Grid Columns**: Number of columns in script grid (2-6)
- **Timeout**: Connection timeout in milliseconds

## Mobile Support

While primarily designed for Android TV, the app also works on mobile devices and tablets. The interface automatically adapts to portrait and landscape orientations, and you can use touch controls instead of a TV remote. The app maintains the same macOS-inspired design and functionality across all devices.

<img src="https://i.imgur.com/jiYBInQ.png" width="15%" align="right" alt="Mobile Portrait View" />

**Mobile Features:**
- **Touch Navigation**: Tap to select and execute scripts
- **Auto Orientation**: Seamlessly switches between portrait and landscape
- **Responsive Layout**: Adapts grid columns based on screen size
- **Same Functionality**: All TV features work on mobile devices

## License

MIT License
