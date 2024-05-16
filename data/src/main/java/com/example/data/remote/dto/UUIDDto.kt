package com.example.data.remote.dto

import java.util.UUID

class UUIDDto(
    val uid: UUID? = null
) {
    companion object {
        fun UUIDtoUUIDDto(uuid: UUID): UUIDDto {
            return UUIDDto(uid = uuid)
        }
    }
}