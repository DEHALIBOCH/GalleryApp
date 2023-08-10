package kz.project.gallery.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Замена для @ClassKey из dagger, для более узкой специализации под ViewModel
 */
@[Target(AnnotationTarget.FUNCTION) Retention(AnnotationRetention.RUNTIME) MapKey]
annotation class ViewModelKey(val value: KClass<out ViewModel>)