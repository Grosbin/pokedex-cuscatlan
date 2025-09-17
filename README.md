# 🔥 Pokémon Cuscatlán

A comprehensive **cross-platform mobile application** with **Android** and **iOS** implementations that allows users to become Pokémon trainers by creating profiles and selecting their team from the first generation of Pokémon.

## 📸 Visual Results

To see the final application results with screenshots and visual demonstration of the project:

🎯 **[Project Visual Demo (PDF)](./pokedex-cuscatlan.pdf)**

## 📱 Platform Support

- **Android**: Built with Kotlin and Jetpack Compose
- **iOS**: Built with Swift and SwiftUI

## 🏗️ Architecture

Both applications follow modern mobile development practices:

### Android Architecture
- **MVVM (Model-View-ViewModel)** architecture pattern
- **Clean Architecture** principles with separation of concerns
- **Modular structure** for scalability and maintainability
- **Dependency Injection** using Dagger Hilt
- **Reactive Programming** with Kotlin Flows and StateFlow

### iOS Architecture
- **MVVM (Model-View-ViewModel)** architecture pattern
- **Clean Architecture** principles with separation of concerns
- **Modular structure** for scalability and maintainability
- **Dependency Injection** using custom container pattern
- **Reactive Programming** with Combine framework

## 🛠️ Tech Stack

### Android
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **Architecture**: MVVM + Clean Architecture

### iOS
- **Language**: Swift
- **UI Framework**: SwiftUI
- **Networking**: URLSession + Combine
- **Serialization**: Codable
- **Image Loading**: AsyncImage
- **Navigation**: NavigationStack
- **Architecture**: MVVM + Clean Architecture

## ✨ Features

### 📋 Trainer Profile Configuration
- **Photo Upload**: Camera or gallery photo selection (optional)
- **Personal Information**: Name (required), favorite hobby (optional), birth date (required)
- **Identification Validation**:
  - **Android**: DUI validation for adults (18+) with format checking and auto-hyphen insertion
  - **iOS**: Simplified numeric DUI validation for adults (18+), no specific format required
  - Minor ID optional for users under 18
- **Real-time Validation**: Form validation with error messages
- **Age Calculation**: Automatic age calculation from birth date

### 🎯 Pokémon Team Selection
- **First Generation Pokémon**: Complete list of 151 original Pokémon from PokeAPI
- **Advanced Search**: Search by name or Pokédex ID
- **Team Limitation**: Select exactly 3 Pokémon for your team
- **Visual Feedback**: Clear selection indicators and counters
- **Pokémon Details**: High-quality sprites from `home.front_default` endpoint

### 👤 Trainer Profile Display
- **Complete Profile**: Display all trainer information and selected team
- **Pokémon Stats**: Detailed stats with progress bars
- **Edit Functionality**: Modify profile information or change Pokémon team
- **Responsive Design**: Adaptive UI for different screen sizes

### 📊 Pokémon Statistics
Accurate stat representation with proper maximum values:
- Health: 255 max
- Attack: 190 max  
- Defense: 230 max
- Special Attack: 194 max
- Special Defense: 230 max
- Speed: 180 max

## 🚀 Getting Started

### Prerequisites

#### For Android Development
- Android Studio Arctic Fox or newer
- Android SDK API 21+ (Android 5.0+)
- Kotlin 1.9+

#### For iOS Development
- Xcode 15.0 or newer
- iOS 16.0+
- Swift 5.9+
- macOS Monterey or newer

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Grosbin/pokemon-cuscatlan
cd pokemon-cuscatlan
```

#### Android Setup
2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select the `android` folder

3. **Build and Run**
   - Wait for Gradle sync to complete
   - Click the "Run" button or press `Ctrl+R`

#### iOS Setup
2. **Open in Xcode**
   - Open Xcode
   - Select "Open a project or file"
   - Navigate to the cloned repository and select `ios/pokedex-cuscatlan.xcodeproj`

3. **Build and Run**
   - Select your target device or simulator
   - Click the "Run" button or press `Cmd+R`

## 🐳 Docker Deployment

For production deployment, use the included Dockerfile:

### Build and Run with Docker

```bash
# Build the Docker image
docker build -t pokemon-cuscatlan .

# Run the container
docker run -p 8080:80 pokemon-cuscatlan
```

### Using Docker Compose

```bash
# Start the application
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
```

Access the application at `http://localhost:8080` to download the APK.

## 🏗️ Project Structure

```
pokemon-cuscatlan/
├── android/                       # Android Implementation
│   ├── app/
│   │   ├── src/main/java/com/cuscatlan/pokemon/
│   │   │   ├── data/
│   │   │   │   ├── api/           # API interfaces
│   │   │   │   ├── model/         # Data models
│   │   │   │   └── repository/    # Repository implementations
│   │   │   ├── di/                # Dependency injection modules
│   │   │   ├── presentation/
│   │   │   │   ├── components/    # Reusable UI components
│   │   │   │   ├── navigation/    # Navigation setup
│   │   │   │   ├── screens/       # UI screens
│   │   │   │   ├── utils/         # Utility functions
│   │   │   │   └── viewmodels/    # ViewModels
│   │   │   ├── ui/theme/          # Material Design theme
│   │   │   ├── MainActivity.kt
│   │   │   └── PokemonApplication.kt
│   │   └── build.gradle.kts
│   ├── gradle/
│   └── build.gradle.kts
├── ios/                           # iOS Implementation
│   ├── pokedex-cuscatlan.xcodeproj
│   └── pokedex-cuscatlan/
│       ├── Models/                # Data models
│       ├── Services/              # Network and repository layer
│       ├── ViewModels/            # ViewModels
│       ├── Views/                 # SwiftUI views
│       ├── Utils/                 # Utility functions
│       ├── PokedexCuscatlanApp.swift
│       └── Info.plist
├── Dockerfile                     # Docker configuration for Android
├── docker-compose.yml            # Docker Compose setup
├── pokedex-cuscatlan.pdf         # Visual demo and screenshots
└── README.md
```

## 🎨 Design Patterns

### Repository Pattern
Abstracts data sources and provides a clean API for data access.

### MVVM Pattern
- **Model**: Data classes and repository implementations
- **View**: Jetpack Compose UI components
- **ViewModel**: UI state management and business logic

### Dependency Injection
Uses Hilt for automatic dependency resolution and improved testability.

### State Management
Utilizes Kotlin Flows and StateFlow for reactive UI updates.

## 🔧 Configuration

### API Integration
The app integrates with the [PokéAPI](https://pokeapi.co/) for Pokémon data:
- Base URL: `https://pokeapi.co/api/v2/`
- Endpoints: `/pokemon` for list, `/pokemon/{id}` for details
- Image source: `sprites.other.home.front_default`

### Form Validation
- **Name**: Minimum 2 characters, required
- **Birth Date**: Must be valid date, not in future, required
- **DUI**: 9-digit format with auto-hyphenation (12345678-9), required for adults
- **Photo**: Required field with gallery/camera selection

## 🛡️ Error Handling

- Network error recovery with retry mechanisms
- Form validation with real-time feedback
- Loading states for better UX
- Graceful fallbacks for missing data

## 🚀 Performance Optimizations

- **Image Caching**: Coil for efficient image loading and caching
- **Lazy Loading**: Virtual scrolling for Pokémon lists
- **State Preservation**: ViewModel survives configuration changes
- **Memory Management**: Proper lifecycle handling

## 🧪 Testing

### Android
Run tests with:
```bash
cd android
./gradlew test
./gradlew connectedAndroidTest
```

### iOS
Run tests with:
```bash
cd ios
xcodebuild test -project pokedex-cuscatlan.xcodeproj -scheme pokedex-cuscatlan -destination 'platform=iOS Simulator,name=iPhone 15'
```

## 📱 Supported Features

- ✅ **Platform**: Android and iOS implementations
- ✅ Form validation with real-time feedback
- ✅ DUI format validation (Android: auto-completion, iOS: numeric validation)
- ✅ Pokémon search by name or ID
- ✅ Team selection (exactly 3 Pokémon)
- ✅ Statistics display with progress bars
- ✅ Profile and team editing
- ✅ Responsive design (Material Design 3 + iOS Design System)
- ✅ Error handling and retry mechanisms
- ✅ Modern navigation (Navigation Compose + NavigationStack)
- ✅ Docker deployment (Android APK)

## 🎯 Bonus Features Implemented

- ✅ **Cross-Platform Development**: Both Android and iOS implementations
- ✅ **Modular Architecture**: Clean separation of concerns on both platforms
- ✅ **Dependency Injection**: Hilt (Android) + Custom DI (iOS)
- ✅ **Docker Support**: Production-ready Dockerfile for Android APK distribution
- ✅ **Advanced UI**: Material Design 3 (Android) + SwiftUI (iOS)
- ✅ **State Management**: Reactive UI with Flows (Android) + Combine (iOS)
- ✅ **Modern Navigation**: Navigation Compose (Android) + NavigationStack (iOS)

## 🌟 Platform-Specific Features

### Android
- Material Design 3 theming
- Hilt dependency injection
- Jetpack Compose UI
- Navigation Compose
- Kotlin Flows + StateFlow

### iOS
- SwiftUI native interface
- NavigationStack for iOS 16+
- Combine reactive framework
- Custom dependency injection
- Modern iOS design patterns

**Built with ❤️ using Kotlin & Jetpack Compose + Swift & SwiftUI**
