package com.example.movieverse.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "movies")
data class MovieInDB(
    @PrimaryKey
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String?,
    // TODO: store videos list in db??
    val hasVideos: Boolean
    //    @ColumnInfo(name = "genre_ids")
//    val genreIds: ArrayList<Int>
    //val genres: List<GenresPair>
)

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

    @Query("SELECT * FROM movies")
    suspend fun getMovieFromDb(): List<MovieInDB>

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun removeMovie(id: Int)

    @Query("SELECT * FROM movies")
    fun getMoviesList(): List<MovieInDB>

    @Query("SELECT id FROM movies")
    fun getMoviesId(): List<Int>

    @Query("DELETE FROM movies")
    fun clearAll()
}

@Database(
    version = 1,
    exportSchema = true,
    entities = [MovieInDB::class, GenreInDB::class]
)
//@TypeConverters(Converters::class)
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

