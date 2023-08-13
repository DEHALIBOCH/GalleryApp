package kz.project.gallery.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kz.project.gallery.presentation.fragment.HomeFragment
import kz.project.gallery.presentation.fragment.MainFragment
import kz.project.gallery.presentation.fragment.PhotoListFragment
import kz.project.gallery.presentation.fragment.SignInFragment
import kz.project.gallery.presentation.fragment.SignUpFragment
import kz.project.gallery.presentation.fragment.SplashFragment
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import javax.inject.Singleton

@[Singleton Component(modules = [AppBindsModule::class, DataModule::class, NetworkModule::class, PresentationModule::class])]
interface AppComponent {

    // TODO - DI!!!

    fun inject(fragment: SignInFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: PhotoListFragment)

    val viewModelFactory: MultiViewModelFactory

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}