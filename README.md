# Android Jetpack

This project uses several Android Jetpack libraries, which are a collection of libraries that help developers follow best practices, reduce boilerplate code, and write code that works consistently across Android versions and devices.

## MVVM Architecture

The project also uses the MVVM architecture which separates the UI code from the business logic and data modeling. This separation of concerns makes the codebase easier to manage and test.

Here's a snippet of how a ViewModel was implemented here:

```kotlin
class HeroViewModel(
    application: Application,
    private val heroRepository: HeroRepository
) : BaseViewModel(application) {

    private val _heroList = MutableLiveData<List<Hero>>()
    ...
}
```

### LiveData

LiveData is used for data observation between the ViewModel and the UI. LiveData ensures that the UI matches the data state and allows automatic updates on the UI upon data state change.

```kotlin
val heroList: LiveData<List<Hero>>
    get() = _heroList
```

## Coroutines

Kotlin Coroutines are used for handling asynchronous tasks, replacing callback-based APIs with a simpler linear code that is easier to read and maintain.

```kotlin
launch {
    try {
        ensureActive()
        val dao = HeroDatabase(getApplication()).heroDao()
        val heroList = dao.getAll()
        ...
    }
}
```

## Room Database

Room is used as an abstraction layer over SQLite. It makes it easier to work with databases in Android. Room takes care of boilerplate code and allows for more compile-time checks of SQL queries.

```kotlin
@Dao
interface HeroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hero: Hero)
    ...
}
```

## Retrofit and RxJava

Retrofit is used for network calls, while RxJava manages background tasks and allows for handling data streams more effectively.

```kotlin
class HeroRepository(private val scheduler: Scheduler = Schedulers.newThread()) {
    fun fetchHeroes(): Single<List<Hero>> {
        return heroService.getHeroes()
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }
}
```

## View Binding

View binding is used to create direct references to views, which eliminates the need for `findViewById()`. View binding can make code more concise, efficient, and safer with compile-time checks to prevent null pointer exceptions.

Example:

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
```

## Data Binding

Data Binding is used to declaratively bind UI components in layouts to data sources, reducing boilerplate code.

```xml
<ImageView
    android:id="@+id/hero_image"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:imageUrl="@{hero.iconUrl}" />
```

## Navigation Component

The Navigation component from Android Jetpack is used to simplify navigation between different screens (fragments) of the app.

```kotlin
val action = HeroFragmentDirections.actionHeroFragmentToDetailsFragment(hero.id)
navController.navigate(action)
```

### Safe Args

The Safe Args plugin for navigation is used to pass data between destinations. It generates simple object and builder classes for type-safe access to arguments specified for destinations and actions.

We retrieve the passed `heroId` safely:

```kotlin
val args: DetailsFragmentArgs by navArgs()
val heroId = args.heroId
```

This ensures type safety and prevents potential crashes due to incorrect data types or missing data when navigating between destinations.

## Kotlin Extension Functions

Kotlin extension functions are used to extend the functionality of classes without having to touch their code. This project includes several extension functions that streamline common tasks. For instance, the `Drawable.kt` and `Image.kt` files contain extension functions for easier image loading and processing.

Example from `Image.kt`:

```kotlin
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.dota2_symbol)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}
```

## Preferences (Settings)

The Android Preferences API is used to store and manage user preferences. This is demonstrated in the `Prefs` utility class and the `SettingsFragment` that uses the `PreferenceFragmentCompat` to create a settings screen from XML.

```xml
<PreferenceScreen xmlns:app="http://schemas.android.com/apk/res-auto">
    <PreferenceCategory app:title="Cache">
        <SwitchPreferenceCompat
            app:key="caching_enabled"
            app:defaultValue="@bool/caching_enabled"
            app:summaryOff="Always calls the backend for fresh information."
            app:summaryOn="Automatically caches data periodically."
            app:title="Enable or disable caching support" />
    </PreferenceCategory>
</PreferenceScreen>
```

## SharedPreferences

The `Prefs` singleton class in `Prefs.kt` provides a neat encapsulation for `SharedPreferences`. It provides a simple API to get and set values, such as saving the last update time and retrieving it, and checking if caching is enabled. This is a good example of the encapsulation principle in SOLID design principles.

```kotlin
class Prefs {
    ...
    fun saveUpdateTime(time: Long) { ... }
    fun getUpdateTime() = prefs?.getLong(PREF_SAVE_TIME, 0) ?: 0
    fun getIsCachingEnabled() = prefs?.getBoolean("caching_enabled", false)
    ...
}
```

## Notifications

`Notifs.kt` shows a good practice for building and displaying a notification. It encapsulates the details of creating a notification, including creating the notification channel, building the notification, and showing it.

```kotlin
class Notifs(val context: Context) {
    ...
    fun createNofitication() { ... }
    private fun createNotificationChannel() { ... }
}
```

## DiffUtil

In `HeroDiffCallback.kt`, the `DiffUtil.Callback` is used to efficiently update a `RecyclerView`. It checks the difference between an old list and a new list and triggers updates for items that have changed or moved. This is a great way to improve the performance of a `RecyclerView`.

```kotlin
class HeroDiffCallback(
    private val oldList: List<Hero>,
    private val newList: List<Hero>
) : DiffUtil.Callback() { ... }
```

## Sharing Intent

Android's built-in sharing intent (`Intent.ACTION_SEND`) is used to share text and streaming media to other apps. This is demonstrated in the `DetailsFragment`, where users can share hero details with others.

```kotlin
val intent = Intent(Intent.ACTION_SEND)
intent.type = "text/plain"
intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this hero!")
intent.putExtra(Intent.EXTRA_TEXT, "Hero details here...")
startActivity(Intent.createChooser(intent, "Share with"))
```

## Palette

The Android Palette library is used to extract prominent colors from images, which can be used to create more visually appealing UIs. The palette is used in the `DetailsFragment` to extract colors from hero images and apply them to the layout background.

## Design Patterns & Best Practices

This Android application project demonstrates several best practices and design patterns in Android development. Below are some of the main ones:

### Model-View-ViewModel (MVVM)

The MVVM design pattern is used to separate the application's user interface logic from the business logic. In this pattern:

- The **Model** represents the data and business logic of the application. In this project, the Model includes data classes such as `Hero` and data handling classes such as `HeroRepository`.
- The **View** represents the user interface and its components. In this project, the View is implemented in classes such as `HeroFragment` and `DetailsFragment`.
- The **ViewModel** acts as a link between the Model and the View. It's responsible for transforming the data from the Model into a form that can be easily displayed in the View. For example, `HeroViewModel` and `DetailsViewModel` in this project.

```kotlin
class HeroViewModel(application: Application, private val heroRepository: HeroRepository) : BaseViewModel(application) {
    ...
}
```

> **Warning**
> **Don't pass a context or activity to a ViewModel in Android's MVVM architecture.**
> 
> This avoids potential memory leaks and adheres to the ViewModel's purpose: to be lifecycle-aware and not hold references to views, like Activities or Fragments.
> A ViewModel can outlive these views, which can cause issues if it directly references them.
> 
> If a context is necessary, such as for database access, use the Application Context.
> This global context isn't tied to a specific lifecycle. The subclass AndroidViewModel provides this, as it includes an Application reference.
> This ensures the ViewModel is tied to the application's lifecycle, not an activity's.
> With this, you get an application-scoped database, no context leaks, and no need for context handling in your ViewModel."

### Repository Pattern

The Repository pattern is used to abstract away the details of data retrieval. It acts as a middleman between the different data sources (like databases and network calls) and the rest of the application. This pattern allows for cleaner and more modular code, and makes it easier to add, remove, or switch between different data sources. An example in this project is the `HeroRepository`.

```kotlin
class HeroRepository(private val scheduler: Scheduler = Schedulers.newThread()) {
    private val heroService = HeroService()

    fun fetchHeroes(): Single<List<Hero>> {
        return heroService.getHeroes()
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }
}
```

### Dependency Injection with Hilt

Hilt is a DI library for Android that reduces the boilerplate of doing manual dependency injection in your project. Hilt is built on top of the popular DI library Dagger to benefit from the compile-time correctness, runtime performance, scalability, and Android Studio support that Dagger provides.

#### Hilt Application

To use Hilt, we need to annotate an Application instance with `@HiltAndroidApp`. Hilt will generate a base class that we can use to access the application-level dependencies.

```kotlin
@HiltAndroidApp
class HiltApplication : Application()
```

The `AndroidManifest.xml` file also needs to be updated. In order to use Hilt, we need to specify the Hilt Application class as the application name in the `AndroidManifest.xml` file.

```xml
<application
    android:name=".di.HiltApplication"
    ...
>
    ...
</application>
```

This is necessary because the Application class serves as the entry point to the application and is responsible for initializing application-level components. By declaring the custom Hilt Application class (in this case `HiltApplication`) in the AndroidManifest.xml, we are instructing the Android system to use this as the Application class when the application is launched.

#### Providing Dependencies

We can provide dependencies via Hilt modules. A module is a class that is annotated with `@Module`. All methods inside the module that are going to provide dependencies should be annotated with `@Provides`. In this project, we provide `HeroDao` and `HeroRepository` in two separate modules: `DatabaseModule` and `RepositoryModule`.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideHeroDao(@ApplicationContext context: Context): HeroDao {
        return HeroDatabase(context).heroDao()
    }
}
```

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideScheduler(): Scheduler = Schedulers.newThread()

    @Singleton
    @Provides
    fun provideHeroRepository(scheduler: Scheduler): HeroRepository = HeroRepository(scheduler)
}
```

The `@InstallIn(SingletonComponent::class)` annotation tells Hilt to install the module in the SingletonComponent, which means these provided dependencies live for the lifetime of the application. They're equivalent to application-scoped dependencies.

The `@Singleton` annotation, when applied to a provider method (`@Provides` annotated method), signifies that the instance created by the method should be created only once and reused each time the dependency is required. So, when the function is called to satisfy a dependency, it will return the same instance every time during the application lifecycle.

#### Injecting Dependencies

Once dependencies are provided, they can be injected into classes. Hilt can provide dependencies to Android classes that have the `@AndroidEntryPoint` annotation. We've injected the `HeroDao` into `DetailsViewModel` and `HeroRepository` into `HeroViewModel`.

```kotlin
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val heroDao: HeroDao,
    application: Application
) : BaseViewModel(application) {
    ...
}
```

```kotlin
@HiltViewModel
class HeroViewModel @Inject constructor (
    private val heroRepository: HeroRepository,
    private val application: Application
) : BaseViewModel(application) {
    ...
}
```

In our `DetailsFragment`, `HeroFragment`, and `MainActivity`, we've used the `by viewModels<ViewModel>` property delegate to get an instance of the ViewModel.

```kotlin
@AndroidEntryPoint
class DetailsFragment : Fragment(), MenuProvider {
    private val viewModel by viewModels<DetailsViewModel>()
    ...
}
```

```kotlin
@AndroidEntryPoint
class HeroFragment : Fragment(), IHeroClick, MenuProvider {
    private val viewModel by viewModels<HeroViewModel>()
   ...
}
```

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    ...
}
```

#### Benefits of Using Hilt

With Hilt, we get several benefits including:

- Simplified configuration
- Reduced boilerplate
- Improved readability
- Increased testing capabilities.

### Data Binding & Extension Functions

The project uses the `@BindingAdapter` annotation from the Data Binding library to create a custom attribute (`android:imageUrl`) that can be used in the XML layout files. This attribute is bound to the `loadImage` function which is an extension function on `ImageView`. This function uses Glide to load an image from a URL into an `ImageView`, showing a circular progress drawable while the image is loading. This makes it much easier and cleaner to load images from a URL into an `ImageView` directly from the XML layout.

Here is the function:

```kotlin
@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}
```

In the layout file, you would use it like this:

```xml
<ImageView
    ...
    android:imageUrl="@{hero.fullImageUrl}"
    ... />
```

This approach leverages the power of Kotlin's extension functions and Android's Data Binding to reduce boilerplate and improve readability.

### Kotlin Coroutines

This project uses Kotlin Coroutines for handling asynchronous tasks, such as database and network operations. Coroutines allow for simpler and more readable asynchronous code, by avoiding callback hell and allowing for straightforward error handling.

### Usage of Android Jetpack Libraries

This project uses several Android Jetpack libraries, which are a collection of libraries that help developers follow best practices, reduce boilerplate code, and write code that works consistently across Android versions and devices. These libraries include:

- **Room**: For data persistence.
- **ViewModel and LiveData**: For lifecycle-aware data storage and observation.
- **Data Binding and View Binding**: For declarative layout and UI code reduction.
- **Navigation Component**: For easier navigation and argument passing between fragments.
- **Preferences**: For user settings.
- **Palette**: For dynamic color extraction from images.

By following these and other best practices and design patterns, this project aims to be a highly maintainable, testable, and scalable Android application.

## Screenshots

<p align="middle">
    <img src="./screenshot/1.jpg" alt="1" width="250"/>
    <img src="./screenshot/2.jpg" alt="2" width="250"/>
    <img src="./screenshot/3.jpg" alt="3" width="250"/>
    <img src="./screenshot/4.jpg" alt="4" width="250"/>
    <img src="./screenshot/5.jpg" alt="5" width="250"/>
</p>