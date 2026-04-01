# Freenow Demo

A demo Android app reproducing the Freenow booking flow.
This project was built to demonstrate modern Android development practices, 
emphasizing a strict **MVI architecture** and reactive UI with **Jetpack Compose**.

## Architecture

The app follows a Unidirectional Data Flow (UDF) powered by the Model-View-Intent (MVI) 
pattern. The project structure is heavily inspired by Google's `Now in Android` (NiA) 
architecture guidelines, separating `core` utilities from `feature` layers.

![Architecture Diagram](docs/mvi_architecture_diagram.png)

## Features

- Native support for English and Spanish (values-es).
- Accessibility support.
- Analytics (ready for Mixpanel/Braze).

## Tech Stack

| Layer                | Technology                                                |
|----------------------|-----------------------------------------------------------|
| UI                   | Jetpack Compose, Material 3                               |
| State Management     | ViewModel, StateFlow, Coroutines                          |
| Dependency Injection | Hilt                                                      |
| Networking           | Retrofit, OkHttp (Configured for Charles Proxy debugging) |
| Animations           | Lottie Compose                                            |
| Testing              | JUnit, Coroutine Test, Turbine                            |
| CI                   | GitHub Actions                                            |
| Maps                 | Google Maps Compose                                       |
| Code Style           | ktlint                                                    |

## Getting Started

### Prerequisites
- Min SDK: 26

### Setup

1. Clone the repository
2. Open the project in Android Studio
3. Add your Google Maps API key to `local.properties` file in the root directory:
```properties
MAPS_API_KEY=your_key_here
```
4. Run the app

## Project Structure
```
app/src/main/java/com/example/freenowdemo/
├── core/
│   ├── analytics/
│   ├── data/
│   ├── designsystem/
│   ├── model/
│   └── network/
├── feature/
│   └── booking/
└── ui/
    ├── components/
    └── navigation/
```

## CI/CD

GitHub Actions runs on every pull request:
- ktlint formatting check
- Unit tests
Fastlane is configured with lanes for automated testing and APK assembly.

## License

For demo purposes only.