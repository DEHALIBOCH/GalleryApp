package kz.project.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse

interface LocalPhotosRepository {

    fun getPopularPhotos(page: Int): Single<PhotoResponse>

    fun getNewPhotos(page: Int): Single<PhotoResponse>

    fun getPhotosByName(name: String, page: Int): Single<PhotoResponse>

    fun getUserPhotos(id: Int, page: Int): Single<PhotoResponse>

    fun getFavoritePhotos(page: Int): Single<PhotoResponse>

    fun getPhotoById(id: Int): Single<Photo>

    fun savePhotoToFavorites(photo: Photo): Completable

    fun removePhotoFromFavorites(photo: Photo): Completable

    fun clearFavorites(): Completable

    fun savePhotos(photos: List<Photo>): Completable
}