package com.github.erguerra.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedActor(
    val movieName: String?,
    val actors: List<Person>?
)
