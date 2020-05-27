package edu.mobile.ebay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import edu.mobile.ebay.entities.Departments;
import edu.mobile.ebay.entities.Products;
import edu.mobile.ebay.restclient.DAO;
import edu.mobile.ebay.restclient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    LinearLayout department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigation = findViewById(R.id.navigationView);
        ActionBarDrawerToggle toggle;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigation.setNavigationItemSelectedListener(this);

        department = findViewById(R.id.departmentslayout);

        DAO service = RetrofitClient.getRetrofitInstance().create(DAO.class);
        Log.i("get departments","start");
        Call<List<Departments>> call = service.getDepartments();
        Log.i("get departments","done");
        call.enqueue(new Callback<List<Departments>>() {
            @Override
            public void onResponse(Call<List<Departments>> call, Response<List<Departments>> response) {
                List<Departments> prod = response.body();
                for(int i = 0; i<prod.size(); i++){
                    TextView textView = new TextView(getBaseContext());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    Log.i("departments name", prod.get(i).getDepartmentName());
                    String data = prod.get(i).getDepartmentName().substring(0, 1).toUpperCase() + prod.get(i).getDepartmentName().substring(1);
                    textView.setText(data);
                    textView.setPadding(50, 70, 50, 70);
                    textView.setTextSize(18);
                    department.addView(textView);

                    final int x = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            String data = prod.get(x).getDepartmentName().substring(0, 1).toUpperCase() + prod.get(x).getDepartmentName().substring(1);
                            bundle.putLong("department", prod.get(x).getDepartmentId());
                            bundle.putString("title", "Showing Department: " + data);
                            ProductsFragment prodfrag = new ProductsFragment();
                            prodfrag.setArguments(bundle);
                            loadFragment(prodfrag);
                        }
                    });

                }
                Log.v("Response: ", response.body().toString());
            }
            @Override
            public void onFailure(Call<List<Departments>> call, Throwable t) {
                Log.v("error: ", t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


        SearchView search = findViewById(R.id.searchView);
        search.setQuery("", false);
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("query", query);
                bundle.putString("title", "Showing Results for: " + query);
                ProductsFragment prodfrag = new ProductsFragment();
                prodfrag.setArguments(bundle);
                search.setQuery("", false);
                search.clearFocus();
                loadFragment(prodfrag);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("title", "Showing All Products");
        ProductsFragment prod = new ProductsFragment();
        prod.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrameProducts, prod)
                .commit();
    }

    public boolean loadFragment(Fragment fragment)
    {
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FrameProducts, fragment).addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Products:
                Bundle bundle = new Bundle();
                bundle.putString("title", "Showing All Products");
                ProductsFragment prod = new ProductsFragment();
                prod.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FrameProducts, prod)
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.sellProduct:
                Intent intent = new Intent(this, SellProduct.class);
                startActivity(intent);
                break;

        }
        return false;
    }
}
