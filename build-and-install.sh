#!/bin/bash

# Keyboard Maestro TV - Build and Install Script
# This script builds the app and installs it on a connected Android TV device

set -e

echo "🎬 Building Keyboard Maestro TV..."

# Clean and build
./gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    
    # Check if ADB is available
    if command -v adb &> /dev/null; then
        echo "📱 Installing on Android TV..."
        adb install -r app/build/outputs/apk/debug/app-debug.apk
        
        if [ $? -eq 0 ]; then
            adb shell am start -n com.pizzaman.keyboardmaestrotv/.MainActivity
            echo "✅ Installation successful!"
            echo "🎉 Keyboard Maestro TV is now installed on your Android TV!"
            echo ""
            echo "Next steps:"
            echo "1. Launch the app from your TV's app launcher"
            echo "2. Go to Settings and configure your Mac's IP address"
            echo "3. Test the connection"
            echo "4. Start controlling your Mac with Keyboard Maestro scripts!"
        else
            echo "❌ Installation failed. Make sure your Android TV is connected via ADB."
            echo "   Run 'adb devices' to check connection."
        fi
    else
        echo "⚠️  ADB not found. Please install Android SDK tools or install manually:"
        echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    fi
else
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi
