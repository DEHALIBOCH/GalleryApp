package kz.project.data.mappers

import kz.project.data.common.Constants
import kz.project.data.remote.dto.photo.ImageDto
import kz.project.data.remote.dto.photo.PhotoDto
import kz.project.data.remote.dto.photo.PhotoResponseDto
import kz.project.data.remote.dto.token.AccessTokenDto
import kz.project.data.remote.dto.user.UserDto
import kz.project.data.remote.dto.user.UserToPostDto
import kz.project.data.remote.dto.user.UserToReceiveDto
import kz.project.domain.model.photo.Image
import kz.project.domain.model.photo.Photo
import kz.project.domain.model.photo.PhotoResponse
import kz.project.domain.model.photo.createEmptyImage
import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.User
import kz.project.domain.model.user.UserToPost

/**
 * Мапит AccessTokenDto из data слоя в AccessToken domain слоя
 */
fun AccessTokenDto.toAccessToken() = AccessToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)

/**
 * Мапит UserToReceiveDto из data слоя в UserToReceive domain слоя
 */
fun UserToReceiveDto.toUser() = User(
    id = id ?: -1,
    email = email ?: "",
    enabled = enabled ?: false,
    phone = phone ?: "",
    fullName = fullName ?: "",
    username = username ?: "",
    birthday = birthday ?: "",
    roles = roles ?: emptyList(),
)


fun UserToPost.toUserToPostDto() = UserToPostDto(
    username = username,
    email = email,
    password = password,
    birthday = birthday,
)


fun UserDto.toUser() = User(
    id = id ?: -1,
    email = email ?: "",
    enabled = enabled ?: false,
    phone = phone ?: "",
    fullName = fullName ?: "",
    username = username ?: "",
    birthday = birthday ?: "",
    roles = roles ?: emptyList(),
)

fun PhotoDto.toPhoto() = Photo(
    dateCreate = dateCreate ?: Constants.EPOCH_TIME,
    description = description ?: "",
    id = id ?: -1,
    image = image?.toImage() ?: createEmptyImage(),
    name = name ?: "",
    new = new ?: true,
    popular = popular ?: true,
    user = user ?: ""
)

fun ImageDto.toImage() = Image(
    id = id,
    name = name,
)

fun PhotoResponseDto.toPhotoResponse() = PhotoResponse(
    countOfPages = countOfPages ?: 0,
    photos = photos?.map { it.toPhoto() } ?: emptyList(),
    itemsPerPage = itemsPerPage ?: 0,
    totalItems = totalItems ?: 0,
)