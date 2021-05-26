package com.github.erguerra.plugins

import com.github.erguerra.data.MoviesDAO
import com.github.erguerra.data.MoviesDAO.Companion.PASSWORD
import com.github.erguerra.data.MoviesDAO.Companion.USER
import com.github.erguerra.data.MoviesDAO.Companion.URI
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    routing {
        val dao = MoviesDAO(URI, USER, PASSWORD)
        get("/movies/amount={amount}") {
            call.parameters["amount"]?.let {
                call.respond(dao.getMovies(it.toInt()))
            }
            call.response.status(HttpStatusCode.BadRequest)
        }
        get("/people/amount={amount}") {
            call.parameters["amount"]?.let {
                call.respond(dao.getPeople(it.toInt()))
            }
            call.response.status(HttpStatusCode.BadRequest)
        }
        get("/movies/actor_name={name}") {
            call.parameters["name"]?.let {
                call.respond(dao.getMoviesByActorName(it))
            }
            call.response.status(HttpStatusCode.BadRequest)
        }
        get("/person/movie_title={title}") {
            call.parameters["title"]?.let {
                call.respond(dao.getPeopleByMovieName(it))
            }
            call.response.status(HttpStatusCode.BadRequest)
        }
    }

}
