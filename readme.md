## Description

This is a small Android app made in a MVVM pattern and that uses data from `https://openweathermap.org/current` to show current weather Data for 5 french cities


## Usage:
1. Get an API key from `https://openweathermap.org/current`
2. Clone the project & set up Android Studio
3. In `com.example.openweatherapitest.network.services.WeatherDataService` replace `API_KEY` by your API key
4. Compile & Install the app to test

## Major libraries used
- RxJava & RxAndroid for asynchronous tasks
- Volley for networking
- Android Architecture Components (ViewModel & LiveData) for easy lyfecycle management
- Gson for data parsing
- Glide for image loading

# Demo 
https://github.com/alex-24/MeteoApp/blob/master/resources/demo.mp4
