package kz.project.data.remote

import io.reactivex.rxjava3.core.Single
import kz.project.data.remote.dto.photo.Image
import kz.project.data.remote.dto.photo.Photo
import kz.project.data.remote.dto.photo.PhotoResponse
import kz.project.data.remote.dto.photo.PhotoUploadForm
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {

    @GET("/api/photos")
    fun getPhotosList(
        @Query("new") new: Boolean?,
        @Query("popular") popular: Boolean?,
        @Query("user.id") userId: List<Int>?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Single<PhotoResponse>

    @GET("/api/photos")
    fun getPhotosByNameList(
        @Query("new") new: Boolean?,
        @Query("popular") popular: Boolean?,
        @Query("name") name: String,
        @Query("user.id") userId: List<Int>?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Single<PhotoResponse>

    @GET("/api/photos/{id}")
    fun getPhotoById(@Path("id") id: Int): Single<Photo>

    //TODO Функционал по загрузке и отправке Фото
    //TODO доделать функционал
    @Multipart
    @POST("/api/media_objects")
    fun postImage(
        @Part image: MultipartBody.Part
    ): Single<Image>

    @POST("/api/photos")
    fun postPhoto(@Body photoUploadForm: PhotoUploadForm): Single<Photo>
}