package com.example.rental_u;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rental_u.Adapters.AdapterProfile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TextView username,userid;
    private int userID;
    private String userName;
    RecyclerView recyclerView;
    AdapterProfile adapter;
    ArrayList<PropertyModel> PropertyModelArrayList =new ArrayList<>();
    Button btnModify;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt("USER_ID", -1);
            userName = getArguments().getString("USERNAME");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.fragment_profile, container, false);
        username = profile.findViewById(R.id.txtUsername);
        username.setText(userName);
        btnModify = profile.findViewById(R.id.btnModify);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of ModifyProfileFragment
                Bundle bundle = new Bundle();
                bundle.putString("UID", String.valueOf(userID));
                bundle.putString("UNAME", userName);

                ModifyProfileFragment modifyProfileFragment = new ModifyProfileFragment();
                modifyProfileFragment.setArguments(bundle);

                // Get a FragmentTransaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with ModifyProfileFragment
                transaction.replace(R.id.scroll_layout, modifyProfileFragment);

                // Add the transaction to the back stack (optional, depends on your requirements)
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });



        recyclerView = profile.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // Move the initialization here
        adapter = new AdapterProfile(PropertyModelArrayList, this.getContext(), new AdapterProfile.ItemClickListener() {
            @Override
            public void onUpdateClick(final int position) {
                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set the dialog title and message
                builder.setTitle("Confirm Update");
                builder.setMessage("Are you sure you want to update this item?");

                // Add buttons to the dialog
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, perform the update operation

                        PropertyModel selectedProperty = PropertyModelArrayList.get(position);
                        String refNo = String.valueOf(selectedProperty.getRef_list_num());
                        String prop_type = String.valueOf(selectedProperty.getProp_type());
                        String bedroom = String.valueOf(selectedProperty.getBedroom());
                        Float price = Float.valueOf(String.valueOf(selectedProperty.getRent_price()));
                        String furniture = String.valueOf(selectedProperty.getFurniture());
                        String remark = String.valueOf(selectedProperty.getRemark());
                        String rep_name = String.valueOf(selectedProperty.getReporter_name());
                        Bitmap imageBitmap = selectedProperty.getImage();
                        byte[] byteArray = getByteArrayFromBitmap(imageBitmap);

                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.UpdateProperty(refNo, prop_type, bedroom, price, furniture, remark, rep_name, byteArray);

                        Toast.makeText(getActivity().getApplicationContext(), "Post is Updated successfully", Toast.LENGTH_SHORT).show();
                        // Notify the adapter that the item at the specified position has changed
                        adapter.notifyItemChanged(position);

                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onDeleteClick(final int position) {
                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set the dialog title and message
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this item?");

                // Add buttons to the dialog
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, perform the delete operation

                        // Retrieve the selected property
                        PropertyModel selectedProperty = PropertyModelArrayList.get(position);

                        // Retrieve RefNo from the selected property
                        String refNo = String.valueOf(selectedProperty.getRef_list_num());

                        // Perform delete operation in the database
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.deleteProperty(refNo, userName);

                        Toast.makeText(getActivity().getApplicationContext(), "Post is Deleted successfully", Toast.LENGTH_SHORT).show();
                        // Remove the item from the list and update the adapter
                        PropertyModelArrayList.remove(position);
                        adapter.notifyItemRemoved(position);

                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);  // Set the adapter after initializing it

        DBHelper dbHelper = new DBHelper(this.getContext());
        ArrayList<PropertyModel> pmArray = dbHelper.readUserProperty(String.valueOf(userName));
        PropertyModelArrayList = pmArray;
        adapter.setPropertyModelArrayList(pmArray);

        return profile;
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }
}