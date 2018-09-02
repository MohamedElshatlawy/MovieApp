package vogella.com.movieapp.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by BinaryWorld on 03-Nov-16.
 */
public class MovieHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="movie.db";
    private static final int DATABASE_VERSION=1;

    private static final String CREATE_TABLE="CREATE TABLE "+MovieContract.getTableName()+" ("
            +MovieContract.getMovieId()+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +MovieContract.getMoviePoster()+" VARCHAR(100) NOT NULL, "
            +MovieContract.getMovieTitle()+" VARCHAR(100) NOT NULL, "
            +MovieContract.getMovieOverview()+" VARCHAR(200) NOT NULL, "
            +MovieContract.getMovieReleaseDate()+" VARCHAR(20) NOT NULL, "
            +MovieContract.getMovieAverage()+" VARCHAR(20) NOT NULL, "
            +MovieContract.getMovieIdTrailer()+" VARCHAR(50)  NULL); ";

    private static final String DROP_TABLE="DROP TABLE IF EXISTS "+MovieContract.getTableName();

    public MovieHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        try {
            db.execSQL(CREATE_TABLE);
        }catch (SQLException e){
            Log.e("OnCreateMethod","Error Create Table: "+e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (SQLException e){
            Log.e("OnUpgradeMethod","Error Drop Table: "+e);

        }
    }
}
