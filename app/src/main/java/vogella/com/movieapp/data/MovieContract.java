package vogella.com.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by BinaryWorld on 03-Nov-16.
 */
public class MovieContract implements BaseColumns {

    private static final String TABLE_NAME="movies";
    private static final String MOVIE_ID=BaseColumns._ID;
    private static final String MOVIE_POSTER="poster";
    private static final String MOVIE_TITLE="title";
    private static final String MOVIE_OVERVIEW="overview";
    private static final String MOVIE_RELEASE_DATE="date";
    private static final String MOVIE_AVERAGE="average";
    private static final String MOVIE_ID_TRAILER="id";

    private static final String MOVIE_AUTHORITY="vogella.com.movieapp";
    private static final Uri BASE_CONTENT_URI=Uri.parse("content://"+MOVIE_AUTHORITY);
    private static final String MOVIE_PATH="movies";
    private static final Uri CONTENT_URI =Uri.withAppendedPath(BASE_CONTENT_URI,MOVIE_PATH);

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getMovieId() {
        return MOVIE_ID;
    }

    public static String getMoviePoster() {
        return MOVIE_POSTER;
    }

    public static String getMovieTitle() {
        return MOVIE_TITLE;
    }

    public static String getMovieOverview() {
        return MOVIE_OVERVIEW;
    }

    public static String getMovieIdTrailer() {return MOVIE_ID_TRAILER;}

    public static String getMovieReleaseDate() {
        return MOVIE_RELEASE_DATE;
    }

    public static String getMovieAverage() {
        return MOVIE_AVERAGE;
    }

    public static String getMovieAuthority() {
        return MOVIE_AUTHORITY;
    }

    public static Uri getBaseContentUri() {
        return BASE_CONTENT_URI;
    }

    public static String getMoviePath() {
        return MOVIE_PATH;
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }
}
