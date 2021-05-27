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
                    id=it[MOVIE][ID].toString().toInt(),
                    title = it[MOVIE][MOVIE_TITLE].toString(),
                    released = it[MOVIE][MOVIE_RELEASED].toString(),
                    tagline = it[MOVIE][MOVIE_TAGLINE].toString(),
                )
            }
        }


    private fun getPersonList(query: Query): List<Person> =
        session.writeTransaction { transaction ->
            transaction.run(query).list {
                Person(
                    id= it[PERSON][ID].toString().toInt(),
                    name = it[PERSON][PERSON_NAME].toString(),
                    born = it[PERSON][PERSON_BORN].toString()
                )
            }
        }


    fun getMovies(amount: Int): List<Movie> {
        val query = Query("MATCH ($MOVIE:Movie) RETURN $MOVIE_RETURN LIMIT $amount")
        return getMovieList(query)
    }

    fun getPeople(amount: Int): List<Person> {
        val query = Query("MATCH ($PERSON:Person) RETURN $PERSON_RETURN as Person LIMIT $amount")
        return getPersonList(query)
    }

    fun getMoviesByActorName(actorName: String): List<Movie> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:ACTED_IN]->($MOVIE: Movie) " +
                    "WHERE $PERSON.name CONTAINS $actorName " +
                    "RETURN $MOVIE_RETURN"
        )
        return getMovieList(query)
    }

    fun getActorsByMovieTitle(movieName: String): List<Person> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:ACTED_IN]->($MOVIE: Movie) " +
                    "WHERE $MOVIE.title CONTAINS $movieName" +
                    "RETURN $PERSON_RETURN"
        )
        return getPersonList(query)
    }

    fun getPeopleWhoActedOnSameMovie(actorName: String): List<Person> {
        val actorsSet: MutableSet<Person> = mutableSetOf()
        val movies = getMoviesByActorName(actorName)
        movies.forEach {
            it.title?.let { movieTitle ->
                actorsSet.addAll(getActorsByMovieTitle(movieTitle))
            }
        }
        return actorsSet.toList()
    }

    fun getMovieByDecade(decade: String): List<Movie> {
        val decadeValue = decade.toInt()
        val query = Query(
            "MATCH ($MOVIE:Movie) " +
                    "WHERE $MOVIE.released >= $decadeValue and $MOVIE.released < ${decadeValue + 10} " +
                    "RETURN $MOVIE_RETURN"
        )
        return getMovieList(query)
    }


    fun getDirectorByMovieTitle(movieTitle: String): List<Person> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:DIRECTED]->($MOVIE: Movie) " +
                    "WHERE $MOVIE.title CONTAINS $movieTitle " +
                    "RETURN $PERSON_RETURN"
        )
        return getPersonList(query)
    }


    fun getMoviesByDirector(directorName: String): List<Movie> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:DIRECTED]->($MOVIE: Movie) " +
                    "WHERE $PERSON.name CONTAINS $directorName " +
                    "RETURN $MOVIE_RETURN"
        )
        return getMovieList(query)
    }

    fun getPeopleDirectedBy(directorName: String): List<Person> {
        val actorsSet: MutableSet<Person> = mutableSetOf()
        val movies = getMoviesByDirector(directorName)
        movies.forEach {
            it.title?.let { movieTitle ->
                actorsSet.addAll(getActorsByMovieTitle(movieTitle))
            }
        }
        return actorsSet.toList()
    }

    fun getRelatedMovies(movieTitle: String): List<Movie> {
        val movieSet: MutableSet<Movie> = mutableSetOf()
        val actors = getActorsByMovieTitle(movieTitle)
        actors.forEach {  person ->
            person.name?.let {
                movieSet.addAll(getMoviesByActorName(it))
            }
        }
        return movieSet.toList()
    }

    //----------------------------- ID -------------------------

    fun getMoviesByActorId(id: Int): List<Movie> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:ACTED_IN]->($MOVIE: Movie) " +
                    "WHERE ID($PERSON) = $id " +
                    "RETURN $MOVIE_RETURN"
        )
        return getMovieList(query)
    }

    fun getActorsByMovieId(id: Int): List<Person> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:ACTED_IN]->($MOVIE: Movie) " +
                    "WHERE ID($MOVIE) = $id" +
                    "RETURN $PERSON_RETURN"
        )
        return getPersonList(query)
    }

    fun getPeopleWhoActedOnSameMovie(actorId: Int): List<Person> {
        val actorsSet: MutableSet<Person> = mutableSetOf()
        val movies = getMoviesByActorId(actorId)
        movies.forEach {
            it.id?.let { movieId ->
                actorsSet.addAll(getActorsByMovieId(movieId))
            }
        }
        return actorsSet.toList()
    }

    fun getDirectorByMovieId(movieId: Int): List<Person> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:DIRECTED]->($MOVIE: Movie) " +
                    "WHERE ID($MOVIE) = $movieId " +
                    "RETURN $PERSON_RETURN"
        )
        return getPersonList(query)
    }

    fun getMoviesByDirectorId(directorId: Int): List<Movie> {
        val query = Query(
            "MATCH ($PERSON:Person)-[:DIRECTED]->($MOVIE: Movie) " +
                    "WHERE ID($PERSON) = $directorId " +
                    "RETURN $MOVIE_RETURN"
        )
        return getMovieList(query)
    }

    fun getPeopleDirectedById(directorId: Int): List<Person> {
        val actorsSet: MutableSet<Person> = mutableSetOf()
        val movies = getMoviesByDirectorId(directorId)
        movies.forEach {
            it.id?.let { movieId ->
                actorsSet.addAll(getActorsByMovieId(movieId))
            }
        }
        return actorsSet.toList()
    }

    fun getRelatedMoviesById(movieId: Int): List<Movie> {
        val movieSet: MutableSet<Movie> = mutableSetOf()
        val actors = getActorsByMovieId(movieId)
        actors.forEach {  person ->
            person.id?.let {
                movieSet.addAll(getMoviesByActorId(it))
            }
        }
        return movieSet.toList()
    }


    override fun close() {
        driver.close()
    }

    companion object {
        const val URI: String = "bolt://localhost:7687"
        const val USER: String = "erguerra"
        const val PASSWORD: String = "erguerra"

        const val ID = "id"

        const val MOVIE: String = "m"
        const val MOVIE_TITLE: String = "title"
        const val MOVIE_RELEASED: String = "released"
        const val MOVIE_TAGLINE: String = "tagline"
        const val MOVIE_RETURN: String = "{ $ID: ID($MOVIE),  $MOVIE_TITLE: $MOVIE.$MOVIE_TITLE, $MOVIE_RELEASED: $MOVIE.$MOVIE_RELEASED, $MOVIE_TAGLINE: $MOVIE.$MOVIE_TAGLINE } as $MOVIE"

        const val PERSON: String = "p"
        const val PERSON_NAME: String = "name"
        const val PERSON_BORN: String = "born"
        const val PERSON_RETURN: String = "{ $ID: ID($PERSON),  $PERSON_NAME: $PERSON.$PERSON_NAME, $PERSON_BORN: $PERSON.$PERSON_BORN } as $PERSON"
    }

}
