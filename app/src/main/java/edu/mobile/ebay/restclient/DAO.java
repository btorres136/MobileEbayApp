package edu.mobile.ebay.restclient;

import java.util.List;

import edu.mobile.ebay.entities.Departments;
import edu.mobile.ebay.entities.JWT;
import edu.mobile.ebay.entities.LoginTemplate;
import edu.mobile.ebay.entities.Products;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DAO {

    @GET("api/Products")
    Call<List<Products>> getAllProducts();

    @GET("api/Products/{id}")
    Call<Products> getProductById(@Path(value = "id") String id);

    @GET("api/Products/Search")
    Call<List<Products>> getProductByName(@Query(value = "name") String name);

    @GET("api/Departments")
    Call<List<Departments>> getDepartments();

    @GET("api/Departments/Products/{id}")
    Call<List<Products>> getProductByDepartment(@Path(value = "id") Long id);

    @POST("auth/addBid")
    Call<Products> addBid(@Header("Authorization") String jwt);

    @POST("api/authenticate")
    Call<JWT> Authenticate(@Body LoginTemplate loginTemplate);

    @Multipart
    @POST("auth/addProduct")
    Call<Void> uploadProduct(@Header("Authorization") String jwt, @Part MultipartBody.Part img, @Part("ProductTitle") RequestBody title, @Part("startingbid") RequestBody price,
                             @Part("department_search") RequestBody department, @Part("status") RequestBody state, @Part("endbid") RequestBody date,
                             @Part("Description") RequestBody description);

}
