package vogella.com.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by BinaryWorld on 03-Nov-16.
 */
public class MovieProvider extends ContentProvider {
    private static MovieHelper movieHelper;

    private static final int MOVIE_LIST=1;
    private static final int MOVIE_LIST_id=2;
    private static UriMatcher URI_MATCHER=getUriMatcher();

    private static UriMatcher getUriMatcher() {

        UriMatcher  uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.getMovieAuthority(),MovieContract.getMoviePath(),MOVIE_LIST);
       // uriMatcher.addURI(MovieContract.getMovieAuthority(),MovieContract.getMoviePath()+"/#",MOVIE_LIST_id);


        return uriMatcher;
    }



    @Override
    public boolean onCreate() {
        movieHelper=new MovieHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        SQLiteDatabase db = movieHelper.getReadableDatabase();

        cursor =db.query(MovieContract.getTableName(),projection,selection,selectionArgs,null,null,sortOrder);

            return cursor;
        }

    @Nullable
    @Override
    public String getType(Uri uri) {

        //"vnd.<uri pattern>/vnd.<name>.<type>"
        switch (URI_MATCHER.match(uri)){
            case MOVIE_LIST:
                return "vnd.android.cursor.dir/vnd."+MovieContract.getMovieAuthority()+"."+MovieContract.getMoviePath();
            case MOVIE_LIST_id:
                return "vnd.android.cursor.item/vnd."+MovieContract.getMovieAuthority()+"."+MovieContract.getMoviePath();
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = movieHelper.getWritableDatabase();
        long id=0;
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_LIST:
                 id=db.insert(MovieContract.getTableName(), null, values);
                break;
        }

        return Uri.withAppendedPath(uri,""+id) ;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = movieHelper.getWritableDatabase();
        int row=db.delete(MovieContract.getTableName(),selection,selectionArgs);
        return row;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = movieHelper.getWritableDatabase();

        int row=db.update(MovieContract.getTableName(),values,selection,selectionArgs);

        return row;
    }
}
