package com.example.rental_u.Adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rental_u.PropertyModel;
import com.example.rental_u.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
public class
AdapterProfile extends RecyclerView.Adapter<AdapterProfile.ViewHolder> {
    Context context;
    ArrayList<PropertyModel> propertyModelArrayList = new ArrayList<>();
    private static final int IMAGE_PERMISSION = 1;
    private static final int RESULT_OK = -1;

    public interface ItemClickListener {
        void onUpdateClick(int position);
        void onDeleteClick(int position);
    }

    private ItemClickListener itemClickListener;

    public AdapterProfile(ArrayList<PropertyModel> propertyModelArrayList, Context context, ItemClickListener itemClickListener) {
        this.propertyModelArrayList = propertyModelArrayList;
        this.context = context;
        this.itemClickListener=itemClickListener;
    }

    public interface AdapterProfileListener {
        void onDeleteClick(String refNo, int position);
        void onUpdateClick(String refNo, String updatedRepName, String updatedPropType, String updatedFurnType, String updatedBedroom, String updatedDate, String updatedPrice, String updatedRemark, byte[] updatedImage, int position);
    }

    private AdapterProfileListener listener;

    public AdapterProfile(ArrayList<PropertyModel> propertyModelArrayList, Context context, ItemClickListener itemClickListener, AdapterProfileListener listener) {
        // ... existing code
        this.propertyModelArrayList = propertyModelArrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void UpdatedProperty(int position, PropertyModel property){
        propertyModelArrayList.set(position,property);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("AdapterProfile", "onBindViewHolder called for position: " + position);
        final PropertyModel propertyList = propertyModelArrayList.get(position);
        holder.RefNo.setText(String.valueOf(propertyList.getRef_list_num()));
        holder.rep_name.setText(propertyList.getReporter_name());
        holder.pro_type.setText(propertyList.getProp_type());
        holder.furn_type.setText(propertyList.getFurniture());
        holder.bedroom.setText(propertyList.getBedroom());
        holder.remak.setText(propertyList.getRemark());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = dateFormat.parse(propertyList.getDate_time());

            // Format the Date object as needed
            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = displayFormat.format(date);

            holder.d_t.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.RentalPrice.setText(String.valueOf(propertyList.getRent_price()));

        holder.ImageView.setImageBitmap(propertyList.getImage());

        String refNo = holder.RefNo.getText().toString();

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ref = Integer.parseInt(holder.RefNo.getText().toString());
                String updatedRepName = holder.rep_name.getText().toString();
                String updatedPropType = holder.pro_type.getText().toString();
                String updatedFurnType = holder.furn_type.getText().toString();
                String updatedBedroom = holder.bedroom.getText().toString();
                String updatedDate = holder.d_t.getText().toString();
                String updatedRentPrice = holder.RentalPrice.getText().toString();
                Float updatedPrice = Float.valueOf(holder.RentalPrice.getText().toString());
                String updatedRemark = holder.remak.getText().toString();
                final Bitmap bitmap1 = ((BitmapDrawable) holder.ImageView.getDrawable()).getBitmap();
                byte[] byteArray = holder.getByteArrayFromBitmap(bitmap1);
                PropertyModel propertyModel = new PropertyModel(ref,updatedPropType,updatedBedroom,updatedDate,updatedPrice,updatedFurnType,updatedRemark,updatedRepName,bitmap1);

                UpdatedProperty(holder.getAdapterPosition(), propertyModel);

                if (itemClickListener != null) {
                    itemClickListener.onUpdateClick(holder.getAdapterPosition());
                }

                // Pass the selected RefNo and position to the ProfileFragment
                if (listener != null) {
                    listener.onUpdateClick(refNo,updatedRepName,updatedPropType,updatedFurnType,updatedBedroom,updatedDate,updatedRentPrice,updatedRemark,byteArray,holder.getAdapterPosition());
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onDeleteClick(holder.getAdapterPosition());
                }

                // Pass the selected RefNo and position to the ProfileFragment
                if (listener != null) {
                    listener.onDeleteClick(refNo, holder.getAdapterPosition());
                }
            }
        });

    }

    public void setPropertyModelArrayList(ArrayList<PropertyModel> propertyModelArrayList) {
        this.propertyModelArrayList = propertyModelArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return propertyModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Activity activity;
        EditText rep_name,pro_type,furn_type,bedroom,RentalPrice,remak,d_t,RefNo,price;
        Button btnUpdate,btnDelete;

        ImageView ImageView;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.activity = activity;

            rep_name =itemView.findViewById(R.id.RePorter_Name);
            pro_type = itemView.findViewById(R.id.PropertyType);
            furn_type =itemView.findViewById(R.id.FurnitureType);
            bedroom = itemView.findViewById(R.id.Bedroom);
            RentalPrice = itemView.findViewById(R.id.Price);
            remak = itemView.findViewById(R.id.Remark);
            d_t = itemView.findViewById(R.id.Date);
            ImageView = itemView.findViewById(R.id.imageView);
            RefNo = itemView.findViewById(R.id.RefNo);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {chooseImage(v);}
            });
        }
        public void chooseImage(View objectView) {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) context).startActivityForResult(intent, IMAGE_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //get byteArray
        private byte[] getByteArrayFromBitmap(Bitmap bitmap){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            Log.d("ByteArrayConversion", "Byte array size: " + byteArray.length);
            return byteArray;
        }
    }
}
