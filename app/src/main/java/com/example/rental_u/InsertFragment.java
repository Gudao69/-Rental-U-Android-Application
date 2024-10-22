package com.example.rental_u;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class InsertFragment extends Fragment {

    private static final int IMAGE_PERMISSION = 1;
    private EditText txtPType, txtBedrooms, txtPrice, txtRemarks, reporter_name, txtFType;
    ImageView imgInsertMenu;

    Bitmap imageToStore;

    private static final int RESULT_OK = -1;
    Bitmap image;
    private static int RESULT_LOAD_IMAGE = 1;



    private int userID;
    private String userName;
    private int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt("USER_ID");
            userName = getArguments().getString("USERNAME");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_frag = inflater.inflate(R.layout.fragment_insert, container, false);
        reporter_name = add_frag.findViewById(R.id.txtRname);
        txtPType = add_frag.findViewById(R.id.txtPType);
        txtBedrooms = add_frag.findViewById(R.id.txtBedroom);
        txtPrice = add_frag.findViewById(R.id.txtPrice);
        txtRemarks = add_frag.findViewById(R.id.txtRemark);
        txtFType = add_frag.findViewById(R.id.txtFurnitType);

        Button btnCreate = add_frag.findViewById(R.id.btnCreate);

        reporter_name.setText(userName);

        imgInsertMenu = add_frag.findViewById(R.id.imgMenuPhoto);

        imgInsertMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(v);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageToStore!=null){
                    if (validate()) {
                        String pType = txtPType.getText().toString();
                        String bedrooms = txtBedrooms.getText().toString();
                        String price = txtPrice.getText().toString();
                        String remarks = txtRemarks.getText().toString();
                        String reportername = reporter_name.getText().toString();
                        String FurnitType = txtFType.getText().toString();
                        float floatPrice = Float.parseFloat(price);
                        final Bitmap bitmap1 = ((BitmapDrawable) imgInsertMenu.getDrawable()).getBitmap();
                        byte[] byteArray = getByteArrayFromBitmap(bitmap1);

                        DBHelper insert_property = new DBHelper(getActivity().getApplicationContext());

                        insert_property.addProperty(pType,bedrooms,floatPrice,FurnitType,remarks,reportername,byteArray);

                        Toast.makeText(getActivity().getApplicationContext(),"Post is Created successfully",Toast.LENGTH_SHORT).show();

                        getParentFragmentManager().beginTransaction().replace(R.id.scroll_layout, new HomeFragment()).commit();

                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Fill All Fields!!!",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Add an Image!!!",Toast.LENGTH_SHORT).show();
                }

            }

            private boolean validate() {
                String nameInput = reporter_name.getText().toString().trim();
                String PtypeInput = txtPType.getText().toString().trim();
                String BedroomInput = txtBedrooms.getText().toString().trim();
                String PriceInput = txtPrice.getText().toString().trim();

                if (nameInput.isEmpty()) {
                    reporter_name.setError("Reporter's Name is required");
                    reporter_name.requestFocus();
                    return false;
                }

                if (PtypeInput.isEmpty()) {
                    txtPType.setError("Property Type is required");
                    txtPType.requestFocus();
                    return false;
                }


                if (BedroomInput.isEmpty()) {
                    txtBedrooms.setError("Bedroom's Type is required");
                    txtBedrooms.requestFocus();
                    return false;
                }

                if (PriceInput.isEmpty()) {
                    txtPrice.setError("Monthly Rental Price is required");
                    txtPrice.requestFocus();
                    return false;
                }

                return true;
            }
        });
        return add_frag;
    }

    public void chooseImage(View objectView){
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*"); //set the type of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_PERMISSION);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == IMAGE_PERMISSION && resultCode == RESULT_OK && data != null && data.getData() != null){
                Uri imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), imagePath);
                imgInsertMenu.setImageBitmap(imageToStore);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //get byteArray
    private byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }
}