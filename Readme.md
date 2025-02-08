# News App

A modern Android application that displays news articles using the News API. Built with clean architecture principles and modern Android development practices.

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 11
- Android SDK 24 or higher

### API Configuration
1. Clone the repository:
   ```bash
   git clone https://github.com/acuon/NewsApp.git
   ```
2. Open the project in Android Studio
3. Get your API key from [News API](https://newsapi.org/register)
4. Create or modify `local.properties` file in the root project directory and add:
   ```properties
   # Add these lines to your existing local.properties file
   BASE_URL=https://newsapi.org/v2/
   API_KEY=your_api_key_here    # Replace with your actual News API key
   ```

   Note: `local.properties` should already be in your `.gitignore` file. If not, add it to prevent committing your API key.

### Building the App
1. Sync project with Gradle files
2. Build the project:
    - Select Build > Make Project
    - Or use shortcut (Ctrl+F9 on Windows/Linux, Cmd+F9 on macOS)

### Running the App
1. Connect an Android device or start an emulator
2. Click the "Run" button (▶️) or use Shift+F10 (Windows/Linux) / Control+R (macOS)

## Project Structure
- MVVM Architecture
- Repository pattern for data management
- Dependency Injection using Hilt
- ViewBinding for view interactions
- Room Database for caching news articles
- Retrofit for API calls

## Libraries Used
- **Hilt**: Dependency injection
- **Retrofit + OkHttp**: API communication
- **Room**: Local database caching
- **Glide**: Image loading
- **Navigation Component**: Navigation management
- **Gson**: JSON parsing
- **ViewBinding**: View interaction
- **DataBinding**: Data binding

## API Implementation
The app uses BuildConfig fields to securely store API configuration:

```kotlin
BuildConfig.BASE_URL    // https://newsapi.org/v2/
BuildConfig.API_KEY      // Your API key
```

These values are accessed from your `local.properties` file through the build.gradle.kts configuration:
```kotlin
buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
buildConfigField("String", "API_KEY", "\"${localProperties["API_KEY"]}\"")
```

## Troubleshooting

### Common Issues
1. "Unauthorized" or "Invalid API key" error:
    - Verify your API key in `local.properties` is correct
    - Check if you've exceeded your API request limit
    - Make sure the API key is being properly passed in network requests

2. BuildConfig errors:
    - Ensure `buildFeatures { buildConfig = true }` is enabled in build.gradle.kts
    - Verify both BASE_URL and API_KEY are defined in local.properties
    - Rebuild project after any changes to local.properties

3. Gradle sync fails:
    - Check local.properties format
    - Verify BASE_URL ends with forward slash
    - Make sure there are no spaces around the = sign in properties

### Local Properties Example
```properties
# Android Studio automatically adds SDK location
sdk.dir=/Users/username/Library/Android/sdk

# News API Configuration
BASE_URL=https://newsapi.org/v2/
API_KEY=1234567890abcdef1234567890abcdef
```

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments
- [News API](https://newsapi.org/) for providing the news data
- All library authors and contributors