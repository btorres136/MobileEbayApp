package edu.mobile.ebay;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.mobile.ebay.entities.Products;
import edu.mobile.ebay.restclient.DAO;
import edu.mobile.ebay.restclient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsFragment extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String query;
    Long department;
    public ProductsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView = rootView.findViewById(R.id.patient_recycle);
        TextView fragtitle = rootView.findViewById(R.id.fragprodtitle);
        fragtitle.setText(getArguments().getString("title"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        Bundle bundle = getArguments();
        query = null;
        if(bundle != null && bundle.getString("query") != null){
            query = (String) bundle.getString("query");
        }
        if(bundle != null && bundle.getLong("department") != 0){
            department =  (Long) bundle.getLong("department");
            Log.i("Department id on PF",department.toString());
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getData(query, department);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getData(query, department);
        return rootView;
    }



    public void getData(@Nullable String query, @Nullable Long Department){
        DAO service = RetrofitClient.getRetrofitInstance().create(DAO.class);
        if(query != null){
            Call<List<Products>> call = service.getProductByName(query);
            call.enqueue(new Callback<List<Products>>() {
                @Override
                public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                    List<Products> prod = response.body();
                    Log.v("data", prod.toString());
                    ProductsAdapter adapter = new ProductsAdapter(getActivity(), prod);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    //Log.v("Response: ", response.body().toString());
                }

                @Override
                public void onFailure(Call<List<Products>> call, Throwable t) {
                    Log.v("error: ", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(Department != null){
            Call<List<Products>> call = service.getProductByDepartment(Department);
            call.enqueue(new Callback<List<Products>>() {
                @Override
                public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                    List<Products> prod = response.body();
                    Log.v("data", prod.toString());
                    ProductsAdapter adapter = new ProductsAdapter(getActivity(), prod);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Products>> call, Throwable t) {
                    Log.v("error: ", t.getMessage());
                }
            });
        }else{
            Call<List<Products>> call = service.getAllProducts();
            call.enqueue(new Callback<List<Products>>() {
                @Override
                public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                    List<Products> prod = response.body();
                    ProductsAdapter adapter = new ProductsAdapter(getActivity(), prod);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    //Log.v("Response: ", response.body().toString());
                }

                @Override
                public void onFailure(Call<List<Products>> call, Throwable t) {
                    Log.v("error: ", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
