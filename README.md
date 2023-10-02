# Uqudo SDK Android Demo Sample App

This project is a sample Android application that demonstrates the usage of the Uqudo SDK for passport onboarding with facial recognition.

## Prerequisites

- Android Studio
- Kotlin

## Setup and Installation

1. Clone the project from the repository.

    ```sh
    git clone https://github.com/uqudo-com/sample-app-android.git
    ```

2. Open the project in Android Studio and let it synchronize.

3. Replace the `ACCESS_TOKEN_HERE` placeholder with the actual access token in the `MainActivity.kt` file.

    ```kotlin
    val authorizationToken = "ACCESS_TOKEN_HERE"
    ```

## Running the app

1. Use a physical device run the app.
2. Click on the 'Run' button in Android Studio to build and run the application.

## Features

- Passport onboarding
- NFC Chip Reading (if available supported by the device)
- Facial recognition
- Handle Enrollment results

## How it works

1. Initialize the `UqudoSDK` in the `onCreate()` method of `MainActivity`.

    ```kotlin
    UqudoSDK.init(applicationContext)
    ```

2. Set up `ActivityResultLauncher` to handle the result of the Uqudo Enrollment process.

3. When the "Start Passport Onboarding" button is clicked, `startEnrollment()` method is invoked to begin the onboarding process.

4. The `parseJWS()` method is used to parse the JWS token received as a result and toast the claims.

## Notes

- Customize the project according to your needs, and refer to the [official Uqudo SDK documentation](http://docs.uqudo.com/docs/) for more details and configurations.
