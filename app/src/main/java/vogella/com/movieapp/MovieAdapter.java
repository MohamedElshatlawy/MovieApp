package vogella.com.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by BinaryWorld on 19-Oct-16.
 */





public class MovieAdapter extends BaseAdapter{
    List<MovieModel> data;
    TextView textView;
    Context context;

    public MovieAdapter(Context context,List<MovieModel> data ) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String BASE_URL="http://image.tmdb.org/t/p/";
        String width="w185";
        String query=data.get(position).getmPoster();

        String url1="http://image.tmdb.org/t/p/"+width+query;
        URL url=null;
        View root=convertView;
        HolderView holderView=null;
        if(root==null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            root= inflater.inflate(R.layout.list_item_main, parent, false);

           holderView =new HolderView(root);
            root.setTag(holderView);


        }else{
            holderView= (HolderView) root.getTag();
        }

        Picasso.with(context).load(url1).resize(220,220).into(holderView.iv);
        return root;

    }

    class HolderView{
        ImageView iv;

        HolderView(View v){
            iv= (ImageView) v.findViewById(R.id.iv);
        }
    }


}


/*
*
*
*
*
*
* */

