package com.github.erguerra.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: Int?,
    val name: String?,
    val born: String?
)
