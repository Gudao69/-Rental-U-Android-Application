package com.example.rental_u;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyProfileFragment extends Fragment {

    EditText txtId,txtUsername,txtEmail,txtPhno,txtPassword;
    Button btnUpdate,btnDelete;

    private String userName,userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("UID");
            userName = getArguments().getString("UNAME");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.fragment_modify_profile, container, false);
        txtId=profile.findViewById(R.id.txtId);
        txtUsername=profile.findViewById(R.id.txtUsername);
        txtEmail=profile.findViewById(R.id.txtEmail);
        txtPhno=profile.findViewById(R.id.txtPhno);
        txtPassword=profile.findViewById(R.id.txtPassword);
        btnUpdate=profile.findViewById(R.id.btnUpdate);
        btnDelete=profile.findViewById(R.id.btnDelete);
        txtId.setText(String.valueOf(userId));
        txtUsername.setText(userName);

        DBHelper dbHelper = new DBHelper(requireContext());
        UserModel userModel = dbHelper.getUserProfile(userId);

        txtEmail.setText(userModel.getEmail());
        txtPhno.setText(userModel.getPhone_no());
        txtPassword.setText(userModel.getPassword());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile();
            }
        });
        return profile;
    }

    private void updateProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Update");
        builder.setMessage("Are you sure you want to update your profile?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, perform the update
                String updatedId = String.valueOf(txtId.getText());
                String updatedUsername = String.valueOf(txtUsername.getText());
                String updatedEmail = String.valueOf(txtEmail.getText());
                String updatedPhone = String.valueOf(txtPhno.getText());
                String updatedPassword = String.valueOf(txtPassword.getText());

                DBHelper dbHelper = new DBHelper(requireContext());
                dbHelper.updateUser(updatedId, updatedEmail, updatedUsername, updatedPhone, updatedPassword);

                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or show a message
                Toast.makeText(requireContext(), "Update canceled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void deleteProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("All the data including your RENTAL POSTS will be deleted! \n Are you sure you want to delete your Account?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, perform the delete
                DBHelper dbHelper = new DBHelper(requireContext());
                dbHelper.deleteUser(userId);
                dbHelper.deleteUserData(userName);

                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

               Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();

                // If you want to navigate to a different screen after deletion, you can do it here
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or show a message
                Toast.makeText(requireContext(), "Deletion canceled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

}