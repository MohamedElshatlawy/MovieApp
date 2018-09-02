package vogella.com.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements Communicator {
    Bundle savedInstanceState;
    boolean checkPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // this.savedInstanceState=savedInstanceState;


        setContentView(R.layout.activity_main);

        FrameLayout pane2= (FrameLayout) findViewById(R.id.pane2);

        if(pane2==null){
            checkPane=false;
        }else{
            checkPane=true;
        }


        if(savedInstanceState==null) {
            MainFragment mainFragment =new MainFragment();
            mainFragment.setCommunicator(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pane1, mainFragment)
                    .commit();
        }
     //loadUI();
    }

    private void loadUI(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortType = prefs.getString("listSort", "popular");

        if((checkInternetConnection()==false)&&(sortType!="favourite")){
           setContentView(R.layout.activity_main_internet_connection);
        }else {
            setContentView(R.layout.activity_main);

            FrameLayout pane2= (FrameLayout) findViewById(R.id.pane2);

            if(pane2==null){
                checkPane=false;
            }else{
                checkPane=true;
            }


            if(savedInstanceState==null) {
                MainFragment mainFragment =new MainFragment();
                mainFragment.setCommunicator(this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.pane1, mainFragment)
                        .commit();
            }
        }
    }

    private boolean checkInternetConnection(){

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;
        }
            return false;
    }

    @Override
    public void dataTransfer(String poster, String title, String overview, String average, String releaseDate, String ID) {

        if(checkPane==false){
            //single pane
            Intent i = new Intent(this, DetailedActivity.class);
                i.putExtra("mPoster",poster);
                i.putExtra("mTitle",title);
                i.putExtra("mOverview",overview);
                i.putExtra("mReleaseDate",releaseDate);
                i.putExtra("mVoteAverage",average);
                i.putExtra("mID",ID);
                startActivity(i);

        }else{
            //two pane
            DetailedFragment detailedFragment =new DetailedFragment();
            Bundle bundle =new Bundle();

            bundle.putString("mPoster",poster);
            bundle.putString("mTitle",title);
            bundle.putString("mOverview",overview);
            bundle.putString("mVoteAverage",average);
            bundle.putString("mReleaseDate",releaseDate);
            bundle.putString("mID",ID);

            detailedFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.pane2,detailedFragment)
                    .commit();


        }
    }

    public void refreshInternet(View view) {
       // onCreate(savedInstanceState);
    loadUI();

    }
}
