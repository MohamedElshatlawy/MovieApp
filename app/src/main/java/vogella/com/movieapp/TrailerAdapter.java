package vogella.com.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by BinaryWorld on 10-Nov-16.
 */
public class TrailerAdapter extends BaseAdapter {
    List<TrailerModel>trailerData;
    Context context;

    public TrailerAdapter(Context context,List<TrailerModel> trailer) {
        this.trailerData = trailer;
        this.context=context;
    }

    @Override
    public int getCount() {
        return trailerData.size();
    }

    @Override
    public Object getItem(int position) {
        return trailerData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View root, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        root= inflater.inflate(R.layout.list_item_trailer, parent, false);

        Button btn= (Button) root.findViewById(R.id.trailer_btn);
        btn.setText("Trailer#"+position+1);

       btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerData.get(position).getKey()));
                    context.startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v="+trailerData.get(position).getKey()));
                    context.startActivity(intent);
                }
            }
        });

        return root;
    }
}
