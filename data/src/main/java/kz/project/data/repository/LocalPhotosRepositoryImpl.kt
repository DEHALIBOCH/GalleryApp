package kz.project.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.runBlocking
import kz.project.data.common.Constants
import kz.project.data.local.entity.PhotoEntity
import kz.project.data.mappers.toPhoto
import kz.project.data.mappers.toPhotoEntity
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.repository.LocalPhotosRepository
import javax.inject.Inject
import kotlin.math.min

class LocalPhotosRepositoryImpl @Inject constructor(
    private val realm: Realm,
    private val backgroundScheduler: Scheduler
) : LocalPhotosRepository {

    override fun getPopularPhotos(page: Int): Single<PhotoResponse> =
        Single.create { emitter ->
            val popularPhotos =
                realm.query<PhotoEntity>(query = "${PhotoEntity::isPopular.name} == $0", true).find()
            if (popularPhotos.isEmpty()) {
                emitter.onError(Exception(Constants.NO_PHOTOS))
            } else {
                emitter.onSuccess(mapRealmResultsToPhotoResponse(popularPhotos, page))
            }
        }.subscribeOn(backgroundScheduler)

    override fun getNewPhotos(page: Int): Single<PhotoResponse> {
        TODO("Not yet implemented")
    }

    override fun getPhotosByName(name: String, page: Int): Single<PhotoResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserPhotos(id: Int, page: Int): Single<PhotoResponse> {
        TODO("Not yet implemented")
    }

    override fun getFavoritePhotos(page: Int): Single<PhotoResponse> {
        TODO("Not yet implemented")
    }

    override fun getPhotoById(id: Int): Single<Photo> {
        TODO("Not yet implemented")
    }

    override fun savePhotoToFavorites(photo: Photo): Completable {
        TODO("Not yet implemented")
    }

    override fun removePhotoFromFavorites(photo: Photo): Completable {
        TODO("Not yet implemented")
    }

    override fun clearFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun savePhotos(photos: List<Photo>): Completable =
        Completable.fromAction {
            runBlocking {
                photos.forEach { photo ->
                    realm.write {
                        this.copyToRealm(PhotoEntity().apply {
                            id = photo.id
                            dateCreate = photo.dateCreate
                            description = photo.description
                            imageName = photo.image.name
                            imageId = photo.image.id
                            name = photo.name
                            isNew = photo.new
                            isPopular = photo.popular
                            user = photo.user
                        }, updatePolicy = UpdatePolicy.ALL)
                    }
                }
            }
        }.subscribeOn(backgroundScheduler)

    private fun mapRealmResultsToPhotoResponse(realmQuery: RealmResults<PhotoEntity>, page: Int): PhotoResponse =
        PhotoResponse(
            countOfPages = realmQuery.size / Constants.LIMIT + 1,
            photos = getNeededPhotosCount(realmQuery, page),
            itemsPerPage = Constants.LIMIT,
            totalItems = realmQuery.size,
        )

    private fun getNeededPhotosCount(realmQuery: RealmResults<PhotoEntity>, page: Int) =
        realmQuery
            .subList(0, min(page * Constants.LIMIT, realmQuery.size))
            .map { photoEntity -> photoEntity.toPhoto() }
            .toMutableList()
}
