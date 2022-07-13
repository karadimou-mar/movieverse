package com.example.movieverse.db

import android.content.Context
import androidx.room.*

@Entity(tableName = "movies")
data class MovieInDB(
    @PrimaryKey
    val id: Int?,
    val title: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String?,
    val genresId: List<Int>,
    val hasVideos: Boolean,
    val popularity: Double,
    val job: String,
)

// todo: remove
@Entity(tableName = "genres")
data class GenreInDB(
    @PrimaryKey
    val id: Int,
    //@ColumnInfo(name = "name")
    val name: String
)

@Entity(tableName = "videos")
data class VideosInDB(
    @PrimaryKey
    val id: String,
    val name: String,
    val key: String?,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String
)

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieInDB: MovieInDB): Long

    @Query("SELECT * FROM MOVIES WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieInDB

    @Query("SELECT * FROM movies")
    fun getMoviesList(): List<MovieInDB>

    @Query("SELECT id FROM movies")
    fun getMoviesId(): List<Int>

    @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :id)")
    fun isFavMovie(id: Int): Boolean

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun removeMovieByID(id: Int)

    @Delete
    fun deleteAllMovies(movies: MovieInDB)
}

@Database(
    version = 3,
    exportSchema = true,
    entities = [MovieInDB::class]
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MovieDatabase

fun getMovieDatabase(context: Context): MovieDatabase {
    synchronized(MovieDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_db"
                )
                // here add migrations later on
                .build()
        }
    }
    return INSTANCE
}

