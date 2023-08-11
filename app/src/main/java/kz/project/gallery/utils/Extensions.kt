package kz.project.gallery.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Следит за вводом в EditText и убирает ошибку с TextInputLayout если пользователь ввел хотя бы 1 символ,
 * так как ошибка мешает взаимодействовать с кнопкой TextInputLayout, паролем например
 */
fun EditText.observeAndRemoveError(textInputLayout: TextInputLayout) = this.textChanges()
    .debounce(100L, TimeUnit.MILLISECONDS)
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe {
        if (it.isNotEmpty()) textInputLayout.error = null
    }


