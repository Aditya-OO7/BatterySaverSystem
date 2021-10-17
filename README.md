# Battery Saver System

## About

Battery saver system is an android application. This application have 3 main features :

- Application list : list of all applications present on device. You can filter this list by user installed apps, system apps or active apps.
- Battery Alarm : Set alarm to certain battery percentage (like 80%) while charging, so that you would be alarmed when battery is optimally charged.
- Battery Alert : Set alert to certain battery percentage (like 30%) while discharging, so that you would be alerted when battery is at emergency capacity.

## Screenshots

<table>
  <tr>
    <td><img src="docs/screenshots/ApplistScreen.png" alt="Application List Screen image"></td>
    <td><img src="docs/screenshots/AlarmScreen.png" alt="Alarm Screen image"></td>
    <td><img src="docs/screenshots/AlertScreen.png" alt="Alert Screen image"></td>
    <td><img src="docs/screenshots/Notificaiton.png" alt="Notification image"></td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/FilterOptions.png" alt="Filter Options image"></td>
    <td><img src="docs/screenshots/AlarmSet.png" alt="Alarm set image"></td>
    <td><img src="docs/screenshots/AlertSet.png" alt="Alert set image"></td>
    <td><img src="docs/screenshots/WhenNotCharging.png" alt="When not charging image"></td>
  </tr>
</table>

## Built with

- Kotlin : First class and official programming language for Android development.
- Android Architecture Components :
  - LiveData : Data types that implement observer pattern.
  - ViewModel : Handles UI logic and helps to maintain state during configuration changes.
- Coroutines : For asynchronous programming
- Navigation components : For better navigation handling.
- Broadcast receiver : for listening to battery state changes.
- Foreground service : to run application in background.
- JUnit4 : Unit testing framework

## Developed with

- [MVVM](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture
- Clean architecture
- Test driven development
- Monolithic architecture

  ![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


## Contribute

If you want to contribute to this app, you're always welcome!
See [Contributing Guidelines](CONTRIBUTING.md).