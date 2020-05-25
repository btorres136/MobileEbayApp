package edu.mobile.ebay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.mobile.ebay.entities.Products;


public class OneProductFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_one_product, container, false);
        Bundle bundle = getArguments();
        Products prod = (Products) bundle.getSerializable("product");
        Log.v("data",prod.getDescription());
        TextView title = rootview.findViewById(R.id.producttitle);
        TextView description = rootview.findViewById(R.id.description);
        TextView status = rootview.findViewById(R.id.status);
        TextView end = rootview.findViewById(R.id.end);
        TextView owner = rootview.findViewById(R.id.owner);
        TextView bid = rootview.findViewById(R.id.bid);
        ImageView image = rootview.findViewById(R.id.prodimage);
        Picasso.get().load("http://192.168.8.131:8080" + prod.getImagePath()).fit().centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(image);

        title.setText(prod.getTitle());
        description.setText(prod.getDescription());
        status.setText(prod.getState());
        bid.setText("$"+prod.getBids().get(0).getQuantity());
        owner.setText(prod.getOwner());
        end.setText(prod.getEndBid().toString());


        return rootview;
    }

}
