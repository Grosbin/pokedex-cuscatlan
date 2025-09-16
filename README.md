# 🔥 Pokémon Trainer App

A comprehensive Android application built with Kotlin and Jetpack Compose that allows users to become Pokémon trainers by creating profiles and selecting their team from the first generation of Pokémon.

## 🏗️ Architecture

The application follows modern Android development practices with:

- **MVVM (Model-View-ViewModel)** architecture pattern
- **Clean Architecture** principles with separation of concerns
- **Modular structure** for scalability and maintainability
- **Dependency Injection** using Dagger Hilt
- **Reactive Programming** with Kotlin Flows and StateFlow

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit + OkHttp
- **Serialization**: Kotlinx Serialization
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **Architecture**: MVVM + Clean Architecture

## ✨ Features

### 📋 Trainer Profile Configuration
- **Photo Upload**: Camera or gallery photo selection (required)
- **Personal Information**: Name (required), favorite hobby, birth date (required)
- **Identification Validation**: 
  - DUI validation for adults (18+) with format checking and auto-hyphen insertion
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
- Android Studio Arctic Fox or newer
- Android SDK API 21+ (Android 5.0+)
- Kotlin 1.9+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/your-username/pokemon-trainer-app.git
cd pokemon-trainer-app
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select the `android` folder

3. **Build and Run**
   - Wait for Gradle sync to complete
   - Click the "Run" button or press `Ctrl+R`

## 🐳 Docker Deployment

For production deployment, use the included Dockerfile:

### Build and Run with Docker

```bash
# Build the Docker image
docker build -t pokemon-trainer-app .

# Run the container
docker run -p 8080:80 pokemon-trainer-app
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
pokemon-trainer-app/
├── android/
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
├── Dockerfile
├── docker-compose.yml
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

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📱 Supported Features

- ✅ Photo capture and selection
- ✅ Form validation with real-time feedback
- ✅ DUI format validation and auto-completion
- ✅ Pokémon search by name or ID
- ✅ Team selection (max 3 Pokémon)
- ✅ Statistics display with progress bars
- ✅ Profile and team editing
- ✅ Responsive design
- ✅ Error handling and retry mechanisms
- ✅ Docker deployment

## 🎯 Bonus Features Implemented

- ✅ **Modular Architecture**: Clean separation of concerns
- ✅ **Dependency Injection**: Hilt implementation
- ✅ **Docker Support**: Production-ready Dockerfile
- ✅ **Advanced UI**: Material Design 3 components
- ✅ **State Management**: Reactive UI with Flows

## 📄 License

This project is part of a technical assessment and follows standard software development practices.

## 🤝 Contributing

This is a technical assessment project. For any questions or suggestions, please contact the development team.

---
**Built with ❤️ using Kotlin & Jetpack Compose**# pokedex-cuscatlan
