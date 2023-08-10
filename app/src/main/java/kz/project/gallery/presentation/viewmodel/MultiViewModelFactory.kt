package kz.project.gallery.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MultiViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator =
            viewModelFactories[modelClass] ?: viewModelFactories.entries.firstOrNull { (key, _) ->
                modelClass.isAssignableFrom(key)
            }?.value ?: throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    val viewModelClasses: Set<Class<out ViewModel>>
        get() = viewModelFactories.keys
}