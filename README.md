# Keyboard Maestro TV

A beautiful Android TV app for controlling Keyboard Maestro scripts on your Mac with macOS-inspired design.

## Screenshots

![Main Screen](https://i.imgur.com/KzYV2T7.png)
*Main screen showing the script grid with shortcut buttons*

![Settings Screen](https://i.imgur.com/vsbRlBJ.png)
*Settings screen for configuring Mac connection*

## Features

- **macOS Design**: Clean interface with macOS colors and typography
- **TV Navigation**: Full D-pad support with focus animations
- **Remote Control**: Execute Keyboard Maestro scripts from your TV
- **Grid Layout**: Configurable columns for script organization

## Setup

### On your Mac:
1. Install and run **Keyboard Maestro**
2. Enable **Web Server** in Preferences → Web Server
3. Note the port (default: 1234)
4. Allow remote access

### Script Configuration:
For scripts to appear in the app, they must have the **"Public Web" trigger** enabled:
1. Open the script in Keyboard Maestro
2. Add a new trigger: **"Public Web"**
3. The script will now be accessible via the web interface
4. Scripts without this trigger will not appear in the app

### On Android TV:
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

## Configuration

- **IP Address**: Your Mac's local network address
- **Port**: Keyboard Maestro web server port (default: 1234)
- **Grid Columns**: Number of columns in script grid (2-6)
- **Timeout**: Connection timeout in milliseconds

## Project Structure

```
app/src/main/
├── java/com/pizzaman/keyboardmaestrotv/
│   ├── MainActivity.kt          # Main screen with script grid
│   ├── SettingsActivity.kt      # Connection settings
│   ├── models/                  # Data classes
│   ├── network/                 # API service
│   └── adapters/                # RecyclerView adapter
├── res/
│   ├── layout/                  # UI layouts
│   ├── drawable/                # Graphics and backgrounds
│   ├── values/                  # Strings, colors, themes
│   └── mipmap-*/                # App icons
└── AndroidManifest.xml
```

## Troubleshooting

**Connection Issues:**
- Verify Keyboard Maestro web server is enabled
- Check network connectivity between devices
- Ensure firewall allows the connection

**Script Issues:**
- Confirm scripts are enabled in Keyboard Maestro
- Ensure scripts have the **"Public Web" trigger** enabled (see Script Configuration above)
- Check Keyboard Maestro logs for errors

## License

MIT License
