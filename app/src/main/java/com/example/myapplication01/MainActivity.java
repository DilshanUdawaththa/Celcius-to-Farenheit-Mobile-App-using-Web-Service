package com.example.myapplication01;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private Button convertBtn, clearBtn;
    private EditText inputTemparature = null;

    private TextView output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertBtn = findViewById(R.id.convertbtn);
        clearBtn = findViewById(R.id.clearbtn);

        inputTemparature = findViewById(R.id.temparatureInput);

        output = findViewById(R.id.output);

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (inputTemparature.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input temperature field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String input = inputTemparature.getText().toString().trim();

                output.setText("Please wait");

                String url = "https://cel-to-faran.herokuapp.com";


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                DataModel modal = new DataModel(input);

                Call<DataModel> call = retrofitAPI.createPost(modal);

                call.enqueue(new Callback<DataModel>() {
                    @Override
                    public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                        DataModel responseFromAPI = response.body();
                        String responseStr = responseFromAPI.getValue();

                        output.setText(responseStr);
                    }

                    @Override
                    public void onFailure(Call<DataModel> call, Throwable t) {
                        output.setText("Temparature from farenheit");
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        System.out.println(t);
                    }
                });
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                output.setText("Temparature from farenheit");
                inputTemparature.setText("");
            }
        });

    }

}