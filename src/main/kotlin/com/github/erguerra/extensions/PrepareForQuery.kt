package com.github.erguerra.extensions

fun String.prepareForQuery() =
    if (this.contains('"'))
        this
    else
        "\"$this\""
