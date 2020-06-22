package edu.mobile.ebay;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import org.json.JSONException;
import org.json.JSONObject;

import edu.mobile.ebay.entities.Departments;
import edu.mobile.ebay.entities.Products;
import edu.mobile.ebay.restclient.DAO;
import edu.mobile.ebay.restclient.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellProduct extends AppCompatActivity {
    EditText state;
    EditText title;
    EditText description;
    EditText endBid;
    EditText startingPrice;
    ImageButton productImage;
    Button Upload;
    Products product;
    Uri image;

    private void showDatePickerDialog(){
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                endBid.setText(selectedDate);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_product_activity);
        state = findViewById(R.id.selledProductState);
        title = findViewById(R.id.selledProductTitle);
        description = findViewById(R.id.selledProductDescription);
        endBid = findViewById(R.id.selledProductEndBid);
        startingPrice  = findViewById(R.id.selledProductStartingPrice);
        productImage = findViewById(R.id.sellingProductImage);
        Upload = findViewById(R.id.selledProductUpload);
        Spinner departmentSpinner = findViewById(R.id.departmentSpinner);
        List<Departments> departments =  (List<Departments>) getIntent().getExtras().getSerializable("departments");
        ArrayList<String> depArrayList = new ArrayList<>();
        for(int i =0; i < departments.size(); i++){
            depArrayList.add(departments.get(i).getDepartmentName().substring(0, 1).toUpperCase() + departments.get(i).getDepartmentName().substring(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,depArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);




        endBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        Upload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.getText() != null && title.getText() != null && description.getText() != null && endBid.getText() != null
                        && startingPrice.getText() != null && productImage.getDrawable() != null){
                    DAO service = RetrofitClient.getRetrofitInstance().create(DAO.class);
                    File file = new File(getRealPathFromURI(image));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part img = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
                    RequestBody description_m = RequestBody.create(MediaType.parse("multipart/form-data"), description.getText().toString());
                    RequestBody title_m = RequestBody.create(MediaType.parse("multipart/form-data"), title.getText().toString());
                    RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), startingPrice.getText().toString());
                    RequestBody state_m = RequestBody.create(MediaType.parse("multipart/form-data"), state.getText().toString());
                    RequestBody date = RequestBody.create(MediaType.parse("multipart/form-data"), endBid.getText().toString());
                    RequestBody department = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                    SharedPreferences shared = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                    String jwt = shared.getString("jwt", "");
                    Call<Void> data = service.uploadProduct(jwt, img, title_m, price, department, state_m, date, description_m);
                    data.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            switch (response.code()){
                                case 200:
                                    Toast.makeText(SellProduct.this, "Upload Finished successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    break;
                                case 401:
                                    Toast.makeText(SellProduct.this, "Sorry you must be a seller to sell a product", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    try {
                                        JSONObject jsonerror = new JSONObject(response.errorBody().string());
                                        if(jsonerror.get("message").toString().startsWith("JWT expired at")){
                                            Toast.makeText(SellProduct.this, "Your Session has expired please Login", Toast.LENGTH_SHORT).show();
                                            Intent login = new Intent(getBaseContext(), Login.class);
                                            startActivity(login);
                                        }
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    Toast.makeText(SellProduct.this, "Something is wrong please Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("data", t.getMessage());
                            Toast.makeText(SellProduct.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(SellProduct.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        productImage.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view.getContext());
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        productImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            image = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            InputStream imageStream = getContentResolver().openInputStream(image);
                            Bitmap SelectedImage = BitmapFactory.decodeStream(imageStream);
                            productImage.setImageBitmap(SelectedImage);
                        }catch (Exception e){
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    private void selectImage(Context context){
        final CharSequence[] options = {"Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a picture");
        builder.setItems(options, (dialog, item) -> {

          if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
