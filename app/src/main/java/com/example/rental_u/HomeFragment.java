package com.example.rental_u;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rental_u.Adapters.Adapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    ArrayList<PropertyModel> PropertyModelArrayList =new ArrayList<>();

    Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = home.findViewById(R.id.recyclerView);


        try {
            adapter = new Adapter(PropertyModelArrayList, this.getContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerView.setAdapter(adapter);

            DBHelper dbHelper = new DBHelper(this.getContext());
            ArrayList<PropertyModel> pmArray = dbHelper.readProperty();
            adapter.setPropertyModelArrayList(pmArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return home;
    }
}