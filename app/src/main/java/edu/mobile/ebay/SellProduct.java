package edu.mobile.ebay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import edu.mobile.ebay.entities.Products;

public class SellProduct extends AppCompatActivity {
    EditText state;
    EditText title;
    EditText description;
    EditText endBid;
    EditText startingPrice;
    ImageView productImage;
    Button Upload;
    Products product;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_product_activity);


        state = (EditText) findViewById(R.id.selledProductState);
        title = (EditText)findViewById(R.id.selledProductTitle);
        description = (EditText)findViewById(R.id.selledProductDescription);
        endBid = (EditText)findViewById(R.id.selledProductEndBid);
        startingPrice  = (EditText)findViewById(R.id.selledProductStartingPrice);
        productImage = (ImageView) findViewById(R.id.sellingProductImage);
        Upload = (Button) findViewById(R.id.selledProductUpload);

        Upload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.getText() != null && title.getText() != null && description.getText() != null && endBid.getText() != null
                        && startingPrice.getText() != null && productImage.getDrawable() != null){
                    Toast.makeText(SellProduct.this, "All is filled", Toast.LENGTH_SHORT).show();

                    //obj to send to web server
                    product = new Products();
                    product.setTitle(title.getText().toString());
                    product.setDescription(description.getText().toString());
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yy").parse(endBid.getText().toString());
                        product.setEndBid(date);
                    }catch (Exception e){
                        Log.e("Exeption on date", e.getMessage());
                    }


                }else{
                    Toast.makeText(SellProduct.this, "Not all is filled", Toast.LENGTH_SHORT).show();
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
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap SelectedImage = BitmapFactory.decodeStream(imageStream);
                            productImage.setImageBitmap(SelectedImage);
                        }catch (Exception e){
                            Toast.makeText(this, "Something ent wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    private void selectImage(Context context){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
