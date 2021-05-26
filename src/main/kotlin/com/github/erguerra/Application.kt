package com.github.erguerra

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.github.erguerra.plugins.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        this.install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                setLenient()
            }
        }
        configureRouting()
    }.start(wait = true)
}


