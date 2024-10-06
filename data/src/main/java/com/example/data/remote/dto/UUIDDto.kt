package com.example.data.remote.dto

import java.util.UUID

class UUIDDto(
    val uid: UUID? = null
) {
    companion object {
        fun uUIDtoUUIDDto(uuid: UUID): UUIDDto {
            return UUIDDto(uid = uuid)
        }

        fun stringToUUIDDto(string: String): UUIDDto {
            return UUIDDto(uid = UUID.fromString(string))
        }
    }
}