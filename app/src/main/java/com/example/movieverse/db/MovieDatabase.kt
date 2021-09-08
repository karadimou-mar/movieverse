package com.example.movieverse.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.*

@Entity(tableName = "movies")
data class MovieInDB(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: Date,
    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int>,
    @ColumnInfo(name = "genres")
    val genres: List<String>?
)

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieInDB: MovieInDB): Long

    @Query("DELETE FROM MovieInDB WHERE id = :id")
    suspend fun removeMovie(id: Int)

    @Query("SELECT * FROM MovieInDB")
    fun getMoviesList(): List<MovieInDB>

    @Query("DELETE FROM MovieInDB")
    fun clearAll()
}

@Database(
    version = 1,
    exportSchema = true,
    entities = [MutableList::class]
)
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

