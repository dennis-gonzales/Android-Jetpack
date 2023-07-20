package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.ActivityMainBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mediaPlayerManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d("COCK", "onCreate: ${mediaPlayerManager.startPlaying()}")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController = findNavController(this, R.id.nav_fragment_container)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}

class MediaPlayerManager @Inject constructor(
    private val playableImpl: IPlayable
) {
    fun startPlaying(): String {
        return "Playing ${playableImpl.play(true)}"
    }
}

class MusicPlayer @Inject constructor(
    private val title: String
) : IPlayable {
    override fun play(playingOnRepeat: Boolean): String {
        return "Music: $title${if (playingOnRepeat) " and is playing on repeat..." else ""}"
    }
}

class VideoPlayer @Inject constructor(
    private val title: String
) : IPlayable {
    override fun play(playingOnRepeat: Boolean): String {
        return "Video: $title${if (playingOnRepeat) " and is playing on repeat..." else ""}"
    }
}

interface IPlayable {
    fun play(playingOnRepeat: Boolean): String
}

@InstallIn(ActivityComponent::class)
@Module
class MyModule {
    @ActivityScoped
    @Provides
    fun providesTitle(): String {
        return "Heaven by Bryan Adams"
    }

    @ActivityScoped
    @Provides
    fun providesPlayable(title: String): IPlayable {
        return MusicPlayer(title)
    }
}

//@InstallIn(ActivityComponent::class)
//@Module
//abstract class MyModule {
//
//    @ActivityScoped
//    @Binds
//    abstract fun bindSomeDependency(
//        someInterfaceImpl: MusicPlayer
//    ): IPlayable
//}