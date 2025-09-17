# Pokédx Cuscatlán - iOS App

Esta es la implementación de iOS de la aplicación Pokédx Cuscatlán, que replica la funcionalidad completa de la versión Android usando SwiftUI.

## 🚀 Características

### ✨ Pokédx Principal
- **Diseño idéntico al Android**: Grid de Pokémon con cards redondeadas
- **Búsqueda en tiempo real**: Por nombre o ID de Pokémon
- **Carga dinámica**: Imágenes y detalles se cargan según sea necesario
- **Navegación fluida**: Hacia detalles de Pokémon individual

### 🎨 Diseño Consistente
- **Colores unificados**: Mismo esquema de colores que Android (`#3B82F6`, `#FFB000`)
- **Tipografía coherente**: Tamaños y pesos de fuente equivalentes
- **Espaciado uniforme**: Padding y margins idénticos
- **Iconografía**: Mismos iconos y elementos visuales

### 👤 Perfil de Entrenador (Opcional)
- **Formulario completo**: Nombre*, foto (opcional), hobby, fecha de nacimiento*, DUI/carnet*
- **Validación DUI**: Algoritmo de El Salvador con formato automático
- **Validación de edad**: Diferencia entre adultos (DUI requerido) y menores (carnet opcional)
- **Selección de fotos**: Integración con biblioteca de fotos nativa

### 🔵 Selección de Equipo Pokémon
- **Cards idénticas**: Mismo diseño que la pantalla principal
- **Selección múltiple**: Máximo 3 Pokémon de la primera generación
- **Indicador visual**: Círculo amarillo con check para Pokémon seleccionados
- **Contador dinámico**: Muestra progreso de selección (X/3)

### 📊 Barras de Estadísticas Dinámicas
- **Colores por tipo**: Las barras usan el color del tipo principal del Pokémon
- **Visualización consistente**: Mismo diseño en detalle y perfil
- **Tipos soportados**: Todos los tipos de la primera generación con colores específicos

### 🧭 Navegación Completa
- **Botones de regreso**: En todas las pantallas con estilo consistente
- **Flujo lógico**: Pokédx → Perfil → Selección → Visualización
- **Modales**: Presentación nativa de iOS con sheets

## 🏗️ Arquitectura

### 📁 Estructura de Archivos
```
ios/pokedex-cuscatlan/
├── Models/
│   └── PokemonModels.swift          # Modelos de datos
├── Services/
│   └── PokemonRepository.swift      # Repositorio de datos (PokeAPI)
├── ViewModels/
│   ├── PokemonListViewModel.swift   # VM para listado y selección
│   ├── PokemonDetailViewModel.swift # VM para detalle individual
│   └── OptionalTrainerFormViewModel.swift # VM para formulario
├── Views/
│   ├── ContentView.swift           # Vista principal con navegación
│   ├── PokedexView.swift          # Pantalla principal del Pokédx
│   ├── PokemonDetailView.swift    # Detalle individual de Pokémon
│   ├── OptionalTrainerFormView.swift # Formulario de perfil
│   ├── PokemonSelectionView.swift   # Selección de equipo
│   └── TrainerProfileView.swift     # Visualización del perfil
├── Utils/
│   ├── ColorExtensions.swift       # Extensiones de colores y tipos
│   └── DUIValidation.swift        # Validación de DUI y carnet
└── PokedexCuscatlanApp.swift      # Punto de entrada de la app
```

### 🏛️ Patrones de Diseño
- **MVVM**: ViewModels manejan lógica de negocio y estado
- **Repository Pattern**: Abstracción de la fuente de datos
- **Combine**: Programación reactiva para actualizaciones de UI
- **SwiftUI**: Framework declarativo nativo de Apple

### 🌐 Integración de APIs
- **PokeAPI**: Consumo de datos de Pokémon (primera generación)
- **Combine Publishers**: Manejo asíncrono de requests HTTP
- **Decodificación JSON**: Modelos Codable nativos
- **Caché en memoria**: Optimización de carga de datos

## 🎨 Elementos Visuales

### 🎯 Colores Principales
- **Azul principal**: `#3B82F6` (Pokédx, iconos)
- **Amarillo**: `#FFB000` (Búsqueda, botones, selección)
- **Fondo**: `#F8F8F8` (Fondo general)
- **Blanco**: Cards y elementos de contenido

### 🃏 Componentes Reutilizables
- **PokemonGridItem**: Card individual de Pokémon
- **PokemonSelectionCard**: Card con selección multiple
- **StatProgressBar**: Barra de estadísticas con color dinámico
- **TypeBadge**: Badge de tipo de Pokémon
- **ProfileDetailCard**: Card de información del perfil

### 📱 Responsividad
- **Grid adaptativo**: 2 columnas en todos los dispositivos
- **Aspect ratio**: 0.85 para cards de Pokémon
- **Scroll views**: Navegación fluida en contenido largo
- **Safe areas**: Respeta áreas seguras de iOS

## 🔧 Funcionalidades Técnicas

### ✅ Validaciones
- **DUI de El Salvador**: Algoritmo completo con dígito verificador
- **Carnet de minoridad**: Validación alfanumérica
- **Fechas**: No futuras, cálculo automático de edad
- **Formularios**: Validación en tiempo real

### 📸 Manejo de Imágenes
- **AsyncImage**: Carga asíncrona nativa de SwiftUI
- **PhotosPicker**: Selección de fotos de la biblioteca
- **Placeholders**: Estados de carga y error
- **Caché automático**: Optimización de memoria

### 🔄 Estados de Carga
- **Loading states**: Indicadores de progreso nativos
- **Error handling**: Manejo elegante de errores de red
- **Retry logic**: Botones de reintentar en caso de fallo
- **Empty states**: Mensajes informativos cuando no hay datos

## 🚀 Compilación y Ejecución

### 📋 Requisitos
- **Xcode 15+**: IDE de desarrollo de Apple
- **iOS 16+**: Versión mínima soportada
- **Swift 5.9+**: Lenguaje de programación
- **SwiftUI 4+**: Framework de UI

### ⚙️ Configuración Inicial
1. Abrir `pokedex-cuscatlan.xcodeproj` en Xcode
2. **Agregar permisos al Info.plist**:
   ```xml
   <key>NSPhotoLibraryUsageDescription</key>
   <string>Esta app necesita acceso a tu biblioteca de fotos para seleccionar una imagen de perfil.</string>

   <key>NSAppTransportSecurity</key>
   <dict>
       <key>NSAllowsArbitraryLoads</key>
       <true/>
   </dict>
   ```
3. Verificar que el **Deployment Target** esté en iOS 16.0+
4. Agregar **PhotosUI** framework si no está incluido

### ▶️ Instrucciones de Ejecución
1. Seleccionar target de dispositivo (simulador o físico)
2. Presionar `Cmd + R` para compilar y ejecutar
3. La app se abrirá en la pantalla principal del Pokédx
4. **Nota**: En simulador, la selección de fotos funcionará con la biblioteca simulada

### 🔧 Solución de Problemas Comunes
- **Error de Codable**: Ya solucionado en los modelos
- **Error de @main duplicado**: Corregido - solo PokedexCuscatlanApp.swift tiene @main
- **Error de compilación "reasonable time"**: Vistas complejas refactorizadas en sub-funciones
- **Error de Combine Types**: Corregido con setFailureType en publishers
- **Permisos de fotos**: Verificar Info.plist tiene NSPhotoLibraryUsageDescription
- **Errores de red**: Verificar NSAppTransportSecurity permite conexiones HTTP

### 🔗 Dependencias
- **Foundation**: Framework base de iOS
- **SwiftUI**: Framework de UI declarativo
- **Combine**: Framework de programación reactiva
- **PhotosUI**: Selector de fotos nativo

## 🆚 Equivalencia con Android

### ✅ Funcionalidades Idénticas
- [x] Pokédx principal con grid y búsqueda
- [x] Detalle de Pokémon con estadísticas dinámicas
- [x] Formulario opcional de perfil de entrenador
- [x] Selección de equipo Pokémon (máx. 3)
- [x] Visualización del perfil completo
- [x] Validación de DUI/carnet por edad
- [x] Navegación con botones de regreso
- [x] Colores consistentes en barras de estadísticas

### 🎨 Diseño Visual
- [x] Mismos colores y tipografía
- [x] Layout idéntico de components
- [x] Iconografía equivalente
- [x] Espaciado y padding uniforme
- [x] Estados de carga y error

### 🔄 Flujo de Navegación
- [x] Pokédx como pantalla principal
- [x] Perfil opcional accesible desde header
- [x] Flujo: Perfil → Selección → Visualización
- [x] Botones de regreso en todas las pantallas
- [x] Edición de perfil y equipo desde visualización

Esta implementación de iOS mantiene 100% de paridad funcional y visual con la versión Android, usando las mejores prácticas y patrones nativos de la plataforma Apple.