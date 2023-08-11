package kz.project.gallery

import android.app.Application
import kz.project.gallery.di.AppComponent
import kz.project.gallery.di.DaggerAppComponent

class GalleryApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}