package kz.project.gallery.di

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
interface PresentationModule {


    companion object {
        //TODO переделать(определить ее в BaseViewModel)
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
    }
}