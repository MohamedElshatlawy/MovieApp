package vogella.com.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vogella.com.movieapp.data.MovieContract;

/**
 * Created by BinaryWorld on 19-Oct-16.
 */
public class DetailedFragment extends Fragment {
    String pref="myPref";
    SharedPreferences sharedPreferences;
    String mID;
    String mPoster;
    String mOverview;
    String mTitle;
    String mReleaseDate;
    String mVoteAverage;

    //String mID=getActivity().getIntent().getExtras().getString("mID");
    List<TrailerModel>trailerData;
    TrailerAdapter trailerAdapter;

    ReviewAdapter reviewAdapter;
    List<ReviewModel>reviewData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.detailed_fragment,container,false);

      //  TextView tv= (TextView) root.findViewById(R.id.list_item_textview_details);
      //  String data = getActivity().getIntent().getExtras().getString("itemName");


        TextView originalTitle= (TextView) root.findViewById(R.id.original_title);
        ImageView movie_poster= (ImageView) root.findViewById(R.id.movie_poster);
        TextView overView= (TextView) root.findViewById(R.id.overview);
        TextView release= (TextView) root.findViewById(R.id.release_date);
        TextView voteRate= (TextView) root.findViewById(R.id.vote_rate);

        final CheckBox favourite = (CheckBox) root.findViewById(R.id.fav);
        final ListView trailer= (ListView) root.findViewById(R.id.trailer_list);
        final ListView review= (ListView) root.findViewById(R.id.review_list);

        Bundle bundle = getArguments();
        if(bundle ==null){
            mPoster = getActivity().getIntent().getExtras().getString("mPoster");
            mTitle = getActivity().getIntent().getExtras().getString("mTitle");
            mOverview = getActivity().getIntent().getExtras().getString("mOverview");
            mReleaseDate =getActivity().getIntent().getExtras().getString("mReleaseDate");
            mVoteAverage =getActivity().getIntent().getExtras().getString("mVoteAverage");
            mID=getActivity().getIntent().getExtras().getString("mID");
        }else {

            mPoster = bundle.getString("mPoster");
            mTitle = bundle.getString("mTitle");
            mOverview = bundle.getString("mOverview");
            mReleaseDate = bundle.getString("mReleaseDate");
            mVoteAverage = bundle.getString("mVoteAverage");
            mID = bundle.getString("mID");
        }
        String BASE_URL="http://image.tmdb.org/t/p/";
        String width="w185";
        String query=mPoster;
        String url1="http://image.tmdb.org/t/p/"+width+query;



        originalTitle.setText(mTitle);
        Picasso.with(getActivity()).load(url1).resize(200,200).into(movie_poster);
        overView.setText(mOverview);
        release.setText(mReleaseDate);
        voteRate.setText(mVoteAverage);

        trailerData=new ArrayList<>();
        trailerAdapter =new TrailerAdapter(getActivity(),trailerData);
        trailer.setAdapter(trailerAdapter);

        reviewData=new ArrayList<>();
        reviewAdapter=new ReviewAdapter(getActivity(),reviewData);
        review.setAdapter(reviewAdapter);

        sharedPreferences=getActivity().getSharedPreferences(pref, getActivity().MODE_PRIVATE);
        favourite.setChecked(sharedPreferences.getBoolean("" + mPoster, false));

        final SharedPreferences.Editor editor =sharedPreferences.edit();

        favourite.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                    if(favourite.isChecked()){
                    editor.putBoolean("" + mPoster, true);
                    editor.commit();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(MovieContract.getMoviePoster(),mPoster);
                    contentValues.put(MovieContract.getMovieTitle(),mTitle);
                    contentValues.put(MovieContract.getMovieOverview(),mOverview);
                    contentValues.put(MovieContract.getMovieReleaseDate(),mReleaseDate);
                    contentValues.put(MovieContract.getMovieAverage(),mVoteAverage);
                    contentValues.put(MovieContract.getMovieIdTrailer(),mID);

                    Uri uri = getActivity().getContentResolver().insert(MovieContract.getContentUri(), contentValues);
                    Toast.makeText(getActivity(),uri.toString(),Toast.LENGTH_LONG).show();

                }else if(!(favourite.isChecked())){
                    editor.putBoolean(""+mPoster,false);
                    editor.commit();
                    int row=getActivity().getContentResolver().delete(MovieContract.getContentUri(),MovieContract.getMoviePoster()+" =?",
                            new String[]{mPoster});

                    Toast.makeText(getContext(),row+" Deleted",Toast.LENGTH_LONG).show();
                       // new MainFragment().movieAutoUpdate();
                }
            }
        });






        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detailedfragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.btn_setting){
            Intent i=new Intent(getActivity(),SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

        @Override
        public void onStart() {
            super.onStart();
            trailerUpdate();
            reviewUpdate();

        }    public void trailerUpdate(){

        FetchTrailer fetchTrailer =new FetchTrailer();
        fetchTrailer.execute(mID);

    }
    public void reviewUpdate(){
        FetchReviews fetchReviews =new FetchReviews();
        fetchReviews.execute(mID);

    }

    public class FetchTrailer extends AsyncTask<String, Void, List<String>> {
        List<String>key;

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        String jsonData = null;


        @Override
        protected List<String> doInBackground(String... params) {
            String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0]+"/videos";
            String KEY_PARAM = "api_key";
            String KEY_PARAM_VAL = "5d50068f857b35c3b6e3975480615b1f";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, KEY_PARAM_VAL)
                    .build();

            try {
                URL url = new URL(builtUri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                jsonData = stringBuffer.toString();
                Log.v("DataAccessed: ", jsonData);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (stringBuffer.length() == 0) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return  getDataFormatJson(jsonData);
        }

        protected List<String> getDataFormatJson(String result) {
            List<String>trailerData =new ArrayList<>();
            try {
                JSONObject jsonObject= new JSONObject(result);
                JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    trailerData.add(jsonObject1.getString("key"));
                 }


            } catch (JSONException e) {
                e.printStackTrace();
            }



            return trailerData;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            super.onPostExecute(results);
            trailerAdapter.trailerData.clear();
            for (int i=0;i<results.size();i++){
                trailerAdapter.trailerData.add(new TrailerModel(results.get(i)));
            }
        trailerAdapter.notifyDataSetChanged();
        }
    }

    public class FetchReviews extends AsyncTask<String, Void, List<String>> {
        List<String>reviewsAuthor;
        List<String>reviewsContent;

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        String jsonData = null;


        @Override
        protected List<String> doInBackground(String... params) {
            String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0]+"/reviews";
            String KEY_PARAM = "api_key";
            String KEY_PARAM_VAL = "5d50068f857b35c3b6e3975480615b1f";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, KEY_PARAM_VAL)
                    .build();

            try {
                URL url = new URL(builtUri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

                jsonData = stringBuffer.toString();
                Log.v("DataAccessed: ", jsonData);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (stringBuffer.length() == 0) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return  getDataFormatJson(jsonData);
        }
        protected List<String> getDataFormatJson(String result) {
            reviewsAuthor=new ArrayList<>();
            reviewsContent=new ArrayList<>();
            try {
                JSONObject jsonObject= new JSONObject(result);
                JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    reviewsAuthor.add(jsonObject1.getString("author"));
                    reviewsContent.add(jsonObject1.getString("content"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



            return reviewsAuthor;
        }

        @Override
        protected void onPostExecute(List<String> authors) {
            super.onPostExecute(authors);
            reviewAdapter.dataReview.clear();
            for (int i=0;i<authors.size();i++){
                reviewData.add(new ReviewModel(reviewsAuthor.get(i),reviewsContent.get(i)));
            }
            reviewAdapter.notifyDataSetChanged();
        }
    }
}
