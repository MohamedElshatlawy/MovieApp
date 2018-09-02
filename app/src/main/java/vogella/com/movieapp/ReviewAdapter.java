package vogella.com.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by BinaryWorld on 25-Nov-16.
 */
public class ReviewAdapter extends BaseAdapter {

    Context context;
    List<ReviewModel>dataReview;
    public ReviewAdapter(Context context,List<ReviewModel>dataReview) {
        this.context=context;
        this.dataReview=dataReview;

    }

    @Override
    public int getCount() {
        return dataReview.size();
    }

    @Override
    public Object getItem(int position) {
        return dataReview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View root, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        root= inflater.inflate(R.layout.list_item_review, parent, false);

        TextView textView = (TextView) root.findViewById(R.id.text_review_author);
        TextView textView2 = (TextView) root.findViewById(R.id.text_review_content);

        textView.setText(dataReview.get(position).getAuthor());
        textView2.setText(dataReview.get(position).getContent());

        return root;
    }
}
