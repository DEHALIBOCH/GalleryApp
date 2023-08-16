package kz.project.gallery.utils

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Реализация "EventBus" на RxJava для передачи поискового запроса из одного фрагмента в другой
 */
object SearchQueryEventBus {

    private val searchTextSubject: PublishSubject<String> = PublishSubject.create()

    fun searchTextObservable(): Observable<String> = searchTextSubject

    fun sendSearchText(query: String) = searchTextSubject.onNext(query)

}