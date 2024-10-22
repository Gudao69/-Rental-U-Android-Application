package com.example.rental_u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardviewProfile extends AppCompatActivity {

    EditText RefNo, PropertyType,Bedroom,Date,Price,FurnitureType,Remark,Reporter_Name;
    Button btnUpdate,btnDelete;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_profile);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toView = new Intent(CardviewProfile.this, Home.class);
                startActivity(toView);
            }
        });
    }
}