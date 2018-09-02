package vogella.com.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_detailed_activity,new DetailedFragment())
                .commit();
    }
}
