# Stock Browser - Android App

**DISCLAIMER**: Work in progress

This project demonstrates how to write a modern android app in 2023. The main goal is to make the app robust, testable, and maintainable using minimal set of well-known libraries and following best practices.

The app itself is very simple. It loads market data from multiple 3rd party APIs and stores them in a local database. Then it allows users to search, browse and mark favorite items.

## Architecture

Architecture follows principles of separation of concerns. App is divided into 3 main layers, every one with its own responsibilities.

- **UI Layer** displays application data on screen using Jetpack Compose. It also holds the state of simple components as text fields. 
- **Domain Layer** is responsible for encapsulating complex business logic, or simple business logic that is used by multiple ViewModels.
- **Data Layer** contains application data and business logic.

App uses **MVI (Model View Intent)** architecture pattern, implemented using Orbit-MVI library. Each screen has a matching ViewModel and ScreenState. ViewModel represents the source of truth for the screen. It handles all user input and returns the corresponding UI state that is displayed on the screen. In addition to state, ViewModel can also produce side effects used mainly for navigation between screens. ViewModel calls different use cases to get data to be displayed to the user. The logic for loading and saving data from different resources is implemented by repositories and data sources.

![App Architecture](screenshots/architecture.png)


## Libraries

- [**Room**](https://developer.android.com/training/data-storage/room) - Abstraction layer over SQLite database
- [**Jetpack Compose**](https://developer.android.com/jetpack/compose) - Android’s recommended modern toolkit for building native UI
- [**Compose Navigation**](https://developer.android.com/jetpack/compose/navigation) - The Navigation component provides support for Jetpack Compose applications.
- [**DataStore**](https://developer.android.com/topic/libraries/architecture/datastore) - Solution that allows you to store key-value pairs or typed objects
- [**Retrofit 2**](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [**kotlinx.serialization**](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform / multi-format reflectionless serialization
- [**Hilt**](https://dagger.dev/hilt/) - Provides a standard way to incorporate Dagger dependency injection into an Android application.
- [**Orbit-MVI**](https://orbit-mvi.org/) - Redux/MVI-like library - but without the baggage
- [**mockito**](https://site.mockito.org/) - Tasty mocking framework for unit tests in Java

## CI/CD
- [**GitHub Actions**](https://docs.github.com/en/actions) - CI/CD platform that allows you to automate your build, test, and deployment pipeline.

## Screenshots

| ![screenshot](screenshots/Screenshot_20221129_162708.png) | ![screenshot](screenshots/Screenshot_20221129_162849.png) | ![screenshot](screenshots/Screenshot_20221129_163100.png) |
| ------- | ----- | ----- |
| ![screenshot](screenshots/Screenshot_20221129_162920.png) | ![screenshot](screenshots/Screenshot_20221129_162906.png) | ![screenshot](screenshots/Screenshot_20221129_163044.png) |


## Video

| <video src="https://user-images.githubusercontent.com/2919625/204576896-42fe4817-75a2-489f-8405-8b6bd1b848ff.mp4" /> | <video src="https://user-images.githubusercontent.com/2919625/204577067-b5c1225e-7537-4c4d-bc78-31758cfdd591.mp4" /> |
| ----- | ----- |

