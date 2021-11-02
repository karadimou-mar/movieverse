import android.os.Parcelable
import com.example.movieverse.model.movie.MovieResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCreditsResponse(
    val cast: List<MovieResponse>,
    val crew: List<MovieResponse>
) : Parcelable
