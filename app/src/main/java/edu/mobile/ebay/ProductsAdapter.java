package edu.mobile.ebay;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.mobile.ebay.entities.Products;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Products> products;
    private FragmentActivity act;

    public ProductsAdapter(FragmentActivity ract, List<Products> rproducts){
        products = rproducts;
        act = ract;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                MainActivity main = (MainActivity) act;
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", products.get(holder.getAdapterPosition()));
                OneProductFragment onefrag = new OneProductFragment();
                onefrag.setArguments(bundle);
                main.loadFragment(onefrag);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        Products product = products.get(position);
        holder.productTitle.setText(product.getTitle());
        holder.productBid.setText("Last Bid: $"  + product.getBids().get(0).getQuantity());
        Picasso.get().load("http://192.168.8.131:8080" + product.getImagePath()).fit().centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(holder.productImage);

    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;
        private TextView productBid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productimage);
            productTitle = itemView.findViewById(R.id.productname);
            productBid = itemView.findViewById(R.id.productbid);
        }
    }
}
