package kz.project.data.mappers

import kz.project.data.remote.dto.token.AccessTokenDto
import kz.project.data.remote.dto.user.UserDto
import kz.project.data.remote.dto.user.UserToPostDto
import kz.project.data.remote.dto.user.UserToReceiveDto
import kz.project.domain.model.token.AccessToken
import kz.project.domain.model.user.User
import kz.project.domain.model.user.UserToPost
import kz.project.domain.model.user.UserToReceive

/**
 * Мапит AccessTokenDto из data слоя в AccessToken domain слоя
 */
fun AccessTokenDto.toAccessToken(): AccessToken {
    return AccessToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}

/**
 * Мапит UserToPostDto из data слоя в UserToPost domain слоя
 */
fun UserToPostDto.toUserToPost(): UserToPost {
    return UserToPost(
        username = username,
        email = email,
        password = password,
        birthday = birthday,
        roles = roles,
    )
}

/**
 * Мапит UserToReceiveDto из data слоя в UserToReceive domain слоя
 */
fun UserToReceiveDto.toUserToReceive(): UserToReceive {
    return UserToReceive(
        id = id ?: -1,
        username = username ?: "",
        email = email ?: "",
        password = password ?: "",
        birthday = birthday,
        roles = roles ?: emptyList(),
    )
}


fun UserToPost.toUserToPostDto(): UserToPostDto {
    return UserToPostDto(
        username = username,
        email = email,
        password = password,
        birthday = birthday,
    )
}

fun UserDto.toCurrentUser() = User(
    id = id ?: -1,
    email = email ?: "",
    enabled = enabled ?: false,
    phone = phone ?: "",
    fullName = fullName ?: "",
    username = username ?: "",
    birthday = birthday ?: "",
    roles = roles ?: emptyList(),
)