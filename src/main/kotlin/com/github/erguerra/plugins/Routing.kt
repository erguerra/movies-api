package com.github.erguerra.plugins

import com.github.erguerra.data.MoviesDAO
import com.github.erguerra.data.MoviesDAO.Companion.PASSWORD
import com.github.erguerra.data.MoviesDAO.Companion.USER
import com.github.erguerra.data.MoviesDAO.Companion.URI
import com.github.erguerra.extensions.prepareForQuery
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {
    routing {
        val dao = MoviesDAO(URI, USER, PASSWORD)
        get("/movies/amount={amount}") {
            call.parameters["amount"]?.let {
                call.respond(dao.getMovies(it.toInt()))
            }
        }
        get("/actors/amount={amount}") {
            call.parameters["amount"]?.let {
                call.respond(dao.getPeople(it.toInt()))
            }
        }
        get("/movies/actor_name={name}") {
            call.parameters["name"]?.let {
                call.respond(dao.getMoviesByActorName(it.prepareForQuery()))
            }
        }
        get("/actors/movie_title={title}") {
            call.parameters["title"]?.let {
                call.respond(dao.getActorsByMovieTitle(it.prepareForQuery()))
            }
        }
        get("/actors/coworkers/name={name}") {
            call.parameters["name"]?.let {
                call.respond(dao.getPeopleWhoActedOnSameMovie(it.prepareForQuery()))
            }
        }
        get("/movies/decade={decade}") {
            call.parameters["decade"]?.let {
                call.respond(dao.getMovieByDecade(it))
            }
        }


        get("/directors/movie_title={movie_title}") {
            call.parameters["movie_title"]?.let {
                call.respond(dao.getDirectorByMovieTitle(it.prepareForQuery()))
            }
        }
        get("/movies/related/movie_title={movie_title}") {
            call.parameters["movie_title"]?.let {
                call.respond(dao.getRelatedMovies(it.prepareForQuery()))
            }
        }
        get("/actors/directed_by={director}") {
            call.parameters["director"]?.let {
                call.respond(dao.getPeopleDirectedBy(it.prepareForQuery()))
            }
        }
        get("/movies/director={director}") {
            call.parameters["director"]?.let {
                call.respond(dao.getMoviesByDirector(it.prepareForQuery()))
            }
        }
        // -------------------------------------------- ID ------------------------------------------------------
        get("/movies/actor_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getMoviesByActorId(it.toInt()))
            }
        }
        get("/actors/movie_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getActorsByMovieId(it.toInt()))
            }
        }
        get("/actors/coworkers/actor_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getPeopleWhoActedOnSameMovie(it.toInt()))
            }
        }


        get("/directors/movie_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getDirectorByMovieId(it.toInt()))
            }
        }
        get("/movies/related/movie_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getRelatedMoviesById(it.toInt()))
            }
        }
        get("/actors/directed_by/director_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getPeopleDirectedById(it.toInt()))
            }
        }
        get("/movies/director_id={id}") {
            call.parameters["id"]?.let {
                call.respond(dao.getMoviesByDirectorId(it.toInt()))
            }
        }
    }

}
