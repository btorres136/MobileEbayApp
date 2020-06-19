package edu.mobile.ebay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.mobile.ebay.entities.JWT;
import edu.mobile.ebay.entities.LoginTemplate;
import edu.mobile.ebay.restclient.DAO;
import edu.mobile.ebay.restclient.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btnLogin);
        Intent main = new Intent(this, MainActivity.class);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView username = findViewById(R.id.txtEmail);
                TextView password = findViewById(R.id.txtPwd);
                if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    LoginTemplate login = new LoginTemplate(username.getText().toString(), password.getText().toString());
                    DAO service = RetrofitClient.getRetrofitInstance().create(DAO.class);
                    Call<JWT> jwt = service.Authenticate(login);

                    jwt.enqueue(new Callback<JWT>() {
                        @Override
                        public void onResponse(Call<JWT> call, Response<JWT> response) {
                            SharedPreferences sharedPreferences = getSharedPreferences("jwt",Context.MODE_PRIVATE);
                            if(response.body() != null){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("jwt",response.body().getToken_type() + " " + response.body().getAccess_token());
                                editor.commit();
                                startActivity(main);
                            }else{
                                Toast.makeText(Login.this,"Authentication failure", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JWT> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(Login.this,"Please provide all the information requested!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}