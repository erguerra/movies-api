package com.github.erguerra.data

import com.github.erguerra.model.Movie
import com.github.erguerra.model.Person
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Query

class MoviesDAO(uri: String, user: String, password: String) : AutoCloseable {
    private val driver: Driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))

    val session = driver.session()

    private fun getMovieList(query: Query): List<Movie> =
        session.writeTransaction { transaction ->
            transaction.run(query).list {
                Movie(
                    title = it[MOVIE_NODE_NAME][MOVIE_TITLE_PROPERTY].toString(),
                    released = it[MOVIE_NODE_NAME][MOVIE_RELEASED_PROPERTY].toString(),
                    tagline = it[MOVIE_NODE_NAME][MOVIE_TAGLINE_PROPERTY].toString(),
                )
            }
        }


    private fun getPersonList(query: Query): List<Person> =
        session.writeTransaction { transaction ->
            transaction.run(query).list {
                Person(
                    name = it[PERSON_NODE_NAME][PERSON_NAME_PROPERTY].toString(),
                    born = it[PERSON_NODE_NAME][PERSON_BORN_PROPERTY].toString()
                )
            }
        }


    fun getMovies(amount: Int): List<Movie> {
        val query = Query("MATCH ($MOVIE_NODE_NAME:Movie) RETURN $MOVIE_NODE_NAME LIMIT $amount")
        return getMovieList(query)
    }

    fun getPeople(amount: Int): List<Person> {
        val query = Query("MATCH ($PERSON_NODE_NAME:Person) RETURN $PERSON_NODE_NAME LIMIT $amount")
        return getPersonList(query)
    }

    fun getMoviesByActorName(actorName: String): List<Movie> {
        val query = Query(
            "MATCH ($PERSON_NODE_NAME:Person)-[:ACTED_IN]->($MOVIE_NODE_NAME: Movie) " +
                    "WHERE p.name CONTAINS '$actorName' " +
                    "RETURN $MOVIE_NODE_NAME"
        )
        return getMovieList(query)
    }

    fun getPeopleByMovieName(movieName: String): List<Person> {
        val query = Query(
            "MATCH ($PERSON_NODE_NAME:Person)-[:ACTED_IN]->($MOVIE_NODE_NAME: Movie) " +
                    "WHERE m.title CONTAINS '$movieName' " +
                    "RETURN $PERSON_NODE_NAME"
        )
        return getPersonList(query)
    }

    fun getPeopleWhoActedOnSameMovie(actorName: String): List<Person> {
        val query = Query(
            "MATCH ($PERSON_NODE_NAME:Person)-[:ACTED_IN]->($MOVIE_NODE_NAME: Movie) " +
                    "WHERE m.title CONTAINS '$movieName' " +
                    "RETURN $PERSON_NODE_NAME"
        )
        return getPersonList(query)
    }


    override fun close() {
        driver.close()
    }

    companion object {
        const val URI: String = "bolt://localhost:7687"
        const val USER: String = "erguerra"
        const val PASSWORD: String = "erguerra"

        const val MOVIE_NODE_NAME: String = "m"
        const val MOVIE_TITLE_PROPERTY: String = "title"
        const val MOVIE_RELEASED_PROPERTY: String = "released"
        const val MOVIE_TAGLINE_PROPERTY: String = "tagline"

        const val PERSON_NODE_NAME: String = "p"
        const val PERSON_NAME_PROPERTY: String = "name"
        const val PERSON_BORN_PROPERTY: String = "born"

    }

}
