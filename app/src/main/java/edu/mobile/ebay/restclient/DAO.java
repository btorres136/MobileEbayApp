package edu.mobile.ebay.restclient;

import java.util.List;

import edu.mobile.ebay.entities.Departments;
import edu.mobile.ebay.entities.Products;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DAO {

    @GET("Products")
    Call<List<Products>> getAllProducts();

    @GET("Products/{id}")
    Call<Products> getProductById(@Path(value = "id") String id);

    @GET("Products/Search")
    Call<List<Products>> getProductByName(@Query(value = "name") String name);

    @GET("Departments")
    Call<List<Departments>> getDepartments();

    @GET("Departments/Products/{id}")
    Call<List<Products>> getProductByDepartment(@Path(value = "id") Long id);

    @POST("addBid")
    Call<Products> addBid();
}
