package com.example.sdahymnal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();





        EditText editText = findViewById(R.id.edit_text);
        Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(view -> {
            try {
                String number = editText.getText().toString();
                Intent intent = new Intent(context, HymnActivity.class);
                intent.putExtra("number", number);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        Button button = findViewById(R.id.like_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HymnActivity.class);

            }
        });


    }

}