# SoUNd - Modern Music Player App

![SoUNd Logo](app\src\main\res\drawable\logo.png)

## 📱 Overview

SoUNd is a modern, feature-rich music player application built with Android Jetpack Compose. It offers a sleek and intuitive user interface combined with powerful music playback capabilities.

## ✨ Features

- 🎵 Modern music playback with ExoPlayer integration
- 🎨 Beautiful Material 3 design with Jetpack Compose
- 📱 Responsive and adaptive UI
- 💾 Local music storage with Room database
- 🌐 Network capabilities with Retrofit
- 🖼️ Efficient image loading with Coil
- 🔄 Background playback support
- 🎯 Navigation using Jetpack Navigation Compose

## 🛠️ Technical Stack

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room
- **Media Player**: ExoPlayer (Media3)
- **Networking**: Retrofit2 + OkHttp3
- **Image Loading**: Coil
- **Dependency Injection**: KSP
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35
- **Language**: Kotlin

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 11 or later
- Android SDK 24 or later

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/2uan2/soUNd.git
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on your device or emulator

## 🏗️ Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/        # Kotlin source files
│   │   ├── res/         # Resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml
│   └── test/           # Unit tests
└── build.gradle.kts    # App-level build configuration
```

## 🧪 Testing

The project includes comprehensive test suites:
- Unit tests using JUnit
- UI tests using Espresso
- Compose UI tests

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Contact

For any queries or suggestions, please open an issue in the repository.

---

Made with ❤️ using Jetpack Compose 