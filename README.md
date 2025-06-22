# KittyCode

Native Android client for [http.cat](https://http.cat) API.

## TODO:

- [x] implement persistent search history
  - [x] move storage access to separate class
- [x] refactor navigation using jetpack-navigation library
- [x] animate image when changing screens (image should move from the center to the top where it appears in the `DetailsActivity`) - *Shared element transitions*
- [ ] fix enter to submit search
- [ ] add opening animation
- [x] change package
- [ ] fix `jvmTarget` deprecation in [build.gradle.kts](app/build.gradle.kts)