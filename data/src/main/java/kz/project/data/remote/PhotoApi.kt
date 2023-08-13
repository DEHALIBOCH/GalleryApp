package kz.project.data.remote

import io.reactivex.rxjava3.core.Single
import kz.project.data.remote.dto.photo.ImageDto
import kz.project.data.remote.dto.photo.PhotoDto
import kz.project.data.remote.dto.photo.PhotoResponseDto
import kz.project.domain.model.photo.PhotoUploadForm
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.nio.ByteOrder

interface PhotoApi {

    @GET("/api/photos")
    fun getPhotosList(
        @Query("new") new: Boolean?,
        @Query("popular") popular: Boolean?,
        @Query("user.id") userId: Int?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("order[id]") order: String = "desc"
    ): Single<PhotoResponseDto>

    @GET("/api/photos")
    fun getPhotosByNameList(
        @Query("name") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("order[id]") order: String = "desc"
    ): Single<PhotoResponseDto>

    @GET("/api/photos/{id}")
    fun getPhotoById(@Path("id") id: Int): Single<PhotoDto>

    @Multipart
    @POST("/api/media_objects")
    fun postImage(
        @Part image: MultipartBody.Part
    ): Single<ImageDto>

    /**
     * images - это не объект images, это - путь "api/media_objects/{id}"
     */
    @POST("/api/photos")
    fun postPhoto(@Body photoUploadForm: PhotoUploadForm): Single<PhotoDto>
}