# Changes in this update

Fixed
- WeatherRepository imported HttpUrlConnection (wrong case), so it did not compile
- Home "Latest Updates" and the Alerts tab always showed SampleData, never real data
- Map always showed SampleData and opened with a fake center selected
- Map barangay filter only listed 4 of the barangays
- Hotlines and Guides screens could not be reached from anywhere
- Alerts tab badge was hardcoded to 2
- Home search bar, bell icon and "See All" did nothing
- Google Maps never opened (resolveActivity returns null on Android 11+ without <queries>)
- Rotation reset the app to the splash screen
- Hardcoded distanceKm = 1.2 on every center

Added
- AppViewModel (shared), DataState, offline cache (SharedPreferences + JSON)
- Status banner (saved data / sample data / error) and weather card with real values only
- Nearest-first sorting and real distances when location is granted
- Status-colored map markers matching the legend
- System back button handling, rememberSaveable navigation state
- Welcome screen only on first launch, shorter splash
- 911 added to hotlines
- Unit tests (distance, filtering, weather parsing, JSON cache, time formatting)
- GitHub Actions workflow, real README

Removed
- Flood/Landslide polygons labeled "MGB". They were hand-typed rectangles, not real hazard data

Changed
- allowBackup is now false
- R8 shrinking enabled for release
- Release builds never show sample data. Debug builds do, with a banner
- pre_launch_test_plan.md statuses corrected to match reality

Not done
- Firebase setup, rules deployment, FCM token and topic handling
- Notification permission request (Android 13+)
- Tagalog / Cebuano strings (all UI text is still hardcoded)
- Admin dashboard is still a mockup
- Not compiled here (no Android SDK in my environment). Run a build first.
