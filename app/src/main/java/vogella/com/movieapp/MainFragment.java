package vogella.com.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MainFragment extends Fragment {

    Communicator communicator;
    List<MovieModel> data;
    //List<MovieModel> dataPoster;
    MovieAdapter adapter;

    List<String> moviePoster;
    List<String> movieTitle;
    List<String> movieOverview;
    List<String> movieReleaseDate;
    List<String> movieVoteAverage;
    List<String> movieID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragmet, container, false);
       // ListView lv= (ListView) root.findViewById(R.id.lv);
        GridView gridView = (GridView) root.findViewById(R.id.lv);

        data = new ArrayList<MovieModel>();
        adapter = new MovieAdapter(getActivity(), data);

        gridView.setAdapter(adapter);
        movieAutoUpdate();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final MovieModel modelObject = (MovieModel) adapter.getItem(position);

                /*Intent i = new Intent(getActivity(), DetailedActivity.class);
                i.putExtra("mPoster", modelObject.getmPoster());
                i.putExtra("mTitle", modelObject.getmTitle());
                i.putExtra("mOverview", modelObject.getmOverview());
                i.putExtra("mReleaseDate", modelObject.getmReleaseDate());
                i.putExtra("mVoteAverage", modelObject.getmVoteAverage());
                i.putExtra("mID",modelObject.getmID());
                startActivity(i);*/

                communicator.dataTransfer(modelObject.getmPoster(),modelObject.getmTitle(),modelObject.getmOverview(),
                        modelObject.getmReleaseDate(),modelObject.getmVoteAverage(),modelObject.getmID());

            }
        });

        return root;
    }

    public void setCommunicator(Communicator communicator){
        this.communicator=communicator;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailedfragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.btn_setting){
            Intent i=new Intent(getActivity(),SettingsActivity.class);
            startActivity(i);
            return true;
       }else if(id==R.id.btn_refresh){
            movieAutoUpdate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class FetchMovieTask extends AsyncTask<String, Void, List<String>> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        String jsonData = null;

        @Override
        protected List<String> doInBackground(String... params) {

            String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0];
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


            return getDataFormatJson(jsonData);
        }

        protected List<String> getDataFormatJson(String result) {

            moviePoster = new ArrayList<String>();
            movieTitle = new ArrayList<String>();
            movieOverview = new ArrayList<String>();
            movieReleaseDate = new ArrayList<String>();
            movieVoteAverage = new ArrayList<String>();
            movieID=new ArrayList<String>();


            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    String moviePosterPath = jsonObject1.getString("poster_path");
                    String movieTitlePath = jsonObject1.getString("original_title");
                    String movieOverviewPath = jsonObject1.getString("overview");
                    String movieReleaseDatePath = jsonObject1.getString("release_date");
                    String movieVoteAveragePath = jsonObject1.getString("vote_average");
                    String movieId=jsonObject1.getString("id");


                    moviePoster.add(moviePosterPath);
                    movieTitle.add(movieTitlePath);
                    movieOverview.add(movieOverviewPath);
                    movieReleaseDate.add(movieReleaseDatePath);
                    movieVoteAverage.add(movieVoteAveragePath);
                    movieID.add(movieId);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviePoster;
        }


        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);


            adapter.data.clear();
            for (int i = 0; i < result.size(); i++) {
                adapter.data.add(new MovieModel(result.get(i), movieTitle.get(i), movieOverview.get(i),
                        movieReleaseDate.get(i), movieVoteAverage.get(i),movieID.get(i)));
            }

            adapter.notifyDataSetChanged();

          }
    }

    private boolean checkInternetConnection(){

        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService (Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;
        }
        return false;
    }

    public void movieAutoUpdate() {
        FetchMovieTask fetchMovieData = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString("listSort", "popular");
        //Toast.makeText(getContext(),sortType, Toast.LENGTH_LONG).show();
        getActivity().setTitle(sortType);
        if((sortType.equals("popular")||sortType.equals("top_rated"))&&(checkInternetConnection()==true)) {

            fetchMovieData.execute(sortType);
            //Toast.makeText(getContext(), "afterFetch", Toast.LENGTH_LONG).show();

        }else if(sortType.equals("favourite")){
            getDataBase();
        }
    }
    public void getDataBase() {
        String[]columns={MovieContract.getMoviePoster(),MovieContract.getMovieTitle(),MovieContract.getMovieOverview()
        ,MovieContract.getMovieReleaseDate(),MovieContract.getMovieAverage(),MovieContract.getMovieIdTrailer()};
       Cursor cursor = getActivity().getContentResolver().query(MovieContract.getContentUri(),
               columns,null,null,null);
      int index1=cursor.getColumnIndex(MovieContract.getMoviePoster());
      int index2=cursor.getColumnIndex(MovieContract.getMovieTitle());
      int index3=cursor.getColumnIndex(MovieContract.getMovieOverview());
      int index4=cursor.getColumnIndex(MovieContract.getMovieReleaseDate());
      int index5=cursor.getColumnIndex(MovieContract.getMovieAverage());
      int index6=cursor.getColumnIndex(MovieContract.getMovieIdTrailer());

        adapter.data.clear();

        while (cursor.moveToNext()){
           adapter.data.add(new MovieModel(cursor.getString(index1),cursor.getString(index2),
                   cursor.getString(index3),cursor.getString(index4),cursor.getString(index5),cursor.getString(index6)));
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        movieAutoUpdate();
    }

}
