package vogella.com.movieapp;

/**
 * Created by BinaryWorld on 19-Oct-16.
 */
public class MovieModel {

    String mPoster;
    String mTitle;
    String mOverview;
    String mReleaseDate;
    String mVoteAverage;
    String mID;

    public MovieModel(String mPoster, String mTitle, String mOverview, String mReleaseDate, String mVoteAverage) {
        this.mPoster = mPoster;
        this.mTitle = mTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
    }

    public MovieModel(String mPoster, String mTitle, String mOverview, String mReleaseDate, String mVoteAverage,String mID) {
        this.mPoster = mPoster;
        this.mTitle = mTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
        this.mID=mID;
    }

    public String getmPoster() {
        return mPoster;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public String getmID() {
        return mID;
    }
}
