package com.therevotech.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token:String
)
