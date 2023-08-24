package kz.project.gallery.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kz.project.data.local.entity.PhotoEntity
import kz.project.data.local.entity.UserEntity
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
interface RealmModule {

    companion object {

        @[Singleton Provides]
        fun provideRealm(@BackgroundScheduler scheduler: Scheduler, context: Context): Realm {
            val future = CompletableFuture<Realm>()

            val config = RealmConfiguration.Builder(
                schema = setOf(
                    PhotoEntity::class, UserEntity::class
                )
            ).build()

            scheduler.scheduleDirect {
                future.complete(Realm.open(config))
            }

            return future.get()
        }

        @[Singleton BackgroundScheduler Provides]
        fun provideBackgroundScheduler(): Scheduler {
            return Schedulers.from(Executors.newSingleThreadExecutor())
        }
    }
}