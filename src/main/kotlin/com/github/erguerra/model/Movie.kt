package com.github.erguerra.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String?,
    val released: String?,
    val tagline: String?,
)
