package edu.mobile.ebay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import edu.mobile.ebay.entities.Products;
import edu.mobile.ebay.restclient.DAO;
import edu.mobile.ebay.restclient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        description.setText("Description: "+prod.getDescription());
        status.setText("Products State: " + prod.getState());
        bid.setText("Highest Bid $"+prod.getBids());
        owner.setText("Seller: " + prod.getOwner());
        end.setText("End of the bid: " + prod.getEndBid().toString());

        Button addbid = rootview.findViewById(R.id.placebid);
        EditText bidset =  rootview.findViewById(R.id.bidset);
        DAO service = RetrofitClient.getRetrofitInstance().create(DAO.class);
        SharedPreferences shared = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String jwt = shared.getString("jwt", "");
        addbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shared.contains("jwt")){

                    if(!bidset.getText().toString().isEmpty()){
                        if(Double.parseDouble(bidset.getText().toString()) > prod.getBids()){
                            Call<Void> data = service.addBid(jwt, prod.getId(), bidset.getText().toString());
                            data.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    switch (response.code()){
                                        case 200:
                                            Toast.makeText(getActivity(), "Bid updated correctly", Toast.LENGTH_SHORT).show();
                                            bid.setText("Highest Bid $"+bidset.getText());
                                            bidset.getText().clear();
                                            break;
                                        case 500:
                                            try {
                                                JSONObject jsonerror = new JSONObject(response.errorBody().string());
                                                if(jsonerror.get("message").toString().startsWith("JWT expired at")){
                                                    Toast.makeText(getActivity(), "Your Session has expired please Login", Toast.LENGTH_SHORT).show();
                                                    Intent login = new Intent(getContext(), Login.class);
                                                    startActivity(login);
                                                }
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "Something is wrong please Login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }else{
                            Toast.makeText(getContext(), "The bid most be higher than the actual bid", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getContext(), "Please add a bid in the addbid text area", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getContext(),"Please Login to access this part", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(getActivity(), Login.class);
                    startActivity(login);
                }
            }
        });
        return rootview;
    }

}
