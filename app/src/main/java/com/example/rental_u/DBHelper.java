package com.example.rental_u;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static String DBName = "RentalUDB.db";
    //Table Names
    private static String USER_TABLE = "user_table";
    private static String Prop_Table = "property_table";

    //USER_TABLE_FIELD
    private static String USER_ID = "user_id";
    private static String USERNAME = "username";
    private static String EMAIL = "email";
    private static String PH_NO = "phone_no";
    private static String PASSWORD = "password";

    //property table data
    private static String Ref_Num = "ref_num";
    private static String Property_Type = "property_type";
    private static String Bedroom = "bedroom";
    private static String Date_Time = "date_time";
    private static String Rent_Price = "rent_price";
    private static String Furniture = "furniture_status";
    private static String Remark = "remark";
    private static String Reporter_Name = "reporter_name";
    private static String Image = "image";


    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_table_create = "CREATE TABLE " + USER_TABLE + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " TEXT," +
                EMAIL + " TEXT, " + PH_NO + " TEXT," + PASSWORD + " TEXT" + ")";


        //creating property table
        String property_table_create = "CREATE TABLE " + Prop_Table + "(" +
                Ref_Num + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Property_Type + " TEXT," + Bedroom + " TEXT," +
                Date_Time + " TEXT," + Rent_Price + " FLOAT," +
                Furniture + " TEXT," + Remark + " TEXT," +
                Reporter_Name + " TEXT," + Image + " BLOB)";

        db.execSQL(user_table_create);
        db.execSQL(property_table_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("drop table if exists " + Prop_Table);
    }

    public void addUser(String username, String email, String ph_no, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USERNAME, username);
        cv.put(EMAIL, email);
        cv.put(PH_NO, ph_no);
        cv.put(PASSWORD, password);

        db.insert(USER_TABLE, null, cv);
        db.close();
    }

    public ArrayList<UserModel> readUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor user_cursor = db.rawQuery("SELECT * FROM " + USER_TABLE, null);
        ArrayList<UserModel> userModelArrayList = new ArrayList<>();

        if (user_cursor.moveToFirst()) {
            do {
                userModelArrayList.add(new UserModel(
                        user_cursor.getInt(0),
                        user_cursor.getString(1),
                        user_cursor.getString(2),
                        user_cursor.getString(3),
                        user_cursor.getString(4)));
            } while (user_cursor.moveToNext());
        }
        if (user_cursor != null) {
            user_cursor.close(); // Close the cursor in the finally block
        }
        return userModelArrayList;
    }

    public void deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, "user_id=?", new String[]{id});
        db.close();
    }

    public void updateUser(String user_id, String email, String username, String phno, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(USER_ID, user_id);
        cv.put(EMAIL, email);
        cv.put(USERNAME, username);
        cv.put(PH_NO, phno);
        cv.put(PASSWORD, password);

        db.update(USER_TABLE, cv, "user_id=?", new String[]{user_id});
        db.close();
    }

    //For Property_Table

    public void addProperty(String propType, String bedroom, Float rentprice, String furniture, String remark, String reporter_name, byte[] image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Property_Type, propType);
        cv.put(Bedroom, bedroom);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        cv.put(Date_Time, String.valueOf(date));
        cv.put(Rent_Price, rentprice);
        cv.put(Furniture, furniture);
        cv.put(Remark, remark);
        cv.put(Reporter_Name, reporter_name);
        cv.put(Image, image);
        db.insert(Prop_Table, null, cv);
        db.close();
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Define the query to check if the username exists
            String query = "SELECT * FROM " + USER_TABLE + " WHERE " + USERNAME + " = ?";
            cursor = db.rawQuery(query, new String[]{username});

            return cursor.getCount() > 0;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Define the query to check if the email exists
            String query = "SELECT * FROM " + USER_TABLE + " WHERE " + EMAIL + " = ?";
            cursor = db.rawQuery(query, new String[]{email});

            // Check if the cursor has any rows, indicating the email exists
            return cursor.getCount() > 0;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean isPhoneNumberExists(String phone_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Define the query to check if the phone number exists
            String query = "SELECT * FROM " + USER_TABLE + " WHERE " + PH_NO+ " = ?";
            cursor = db.rawQuery(query, new String[]{phone_no});

            // Check if the cursor has any rows, indicating the phone number exists
            return cursor.getCount() > 0;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public ArrayList<PropertyModel> readProperty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor prop_cursor = db.rawQuery("select * from " + Prop_Table, null);
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

        if (prop_cursor.moveToFirst()) {
            do {
                // Get the image as a byte array
                byte[] imageByteArray = prop_cursor.getBlob(8);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                propModelArrayList.add(new PropertyModel(
                        prop_cursor.getInt(0),
                        prop_cursor.getString(1),
                        prop_cursor.getString(2),
                        prop_cursor.getString(3),
                        prop_cursor.getFloat(4),
                        prop_cursor.getString(5),
                        prop_cursor.getString(6),
                        prop_cursor.getString(7),
                        imageBitmap
                ));
            } while (prop_cursor.moveToNext());
        }
        if (prop_cursor != null) {
            prop_cursor.close(); // Close the cursor in the finally block
        }
        return propModelArrayList;
    }

    public ArrayList<PropertyModel> readUserProperty(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor prop_cursor = db.rawQuery("select * from " + Prop_Table + " WHERE reporter_name = ?", new String[]{username});
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

            if (prop_cursor.moveToFirst()) {
                do {
                    // Get the image as a byte array
                    byte[] imageByteArray = prop_cursor.getBlob(8);

                    // Convert byte array to Bitmap
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                    propModelArrayList.add(new PropertyModel(
                            prop_cursor.getInt(0),
                            prop_cursor.getString(1),
                            prop_cursor.getString(2),
                            prop_cursor.getString(3),
                            prop_cursor.getFloat(4),
                            prop_cursor.getString(5),
                            prop_cursor.getString(6),
                            prop_cursor.getString(7),
                            imageBitmap
                    ));
                } while (prop_cursor.moveToNext());
            }
            if (prop_cursor != null) {
                prop_cursor.close();
            }
        return propModelArrayList;
    }

    public ArrayList<PropertyModel> searchproperty(String id, String name){
        SQLiteDatabase db = getReadableDatabase();
        Cursor prop_cursor = db.rawQuery("SELECT * FROM " + Prop_Table + " WHERE " + Ref_Num + " = ? AND " + Reporter_Name + " = ?", new String[]{id, name});
        ArrayList<PropertyModel> propModelArrayList = new ArrayList<>();

        if(prop_cursor.moveToFirst()){
            do {
                // Get the image as a byte array
                byte[] imageByteArray = prop_cursor.getBlob(8);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                propModelArrayList.add(new PropertyModel(
                        prop_cursor.getInt(0),
                        prop_cursor.getString(1),
                        prop_cursor.getString(2),
                        prop_cursor.getString(3),
                        prop_cursor.getFloat(4),
                        prop_cursor.getString(5),
                        prop_cursor.getString(6),
                        prop_cursor.getString(7),
                        imageBitmap
                ));
            }while(prop_cursor.moveToNext());
        }
        return propModelArrayList;
    }

    public void deleteProperty(String id,String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Prop_Table,"ref_num=? AND reporter_name=?",new String[]{id, name});
        db.close();
    }

    public void deleteUserData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Prop_Table, "reporter_name=?", new String[]{username});
        db.close();
    }

    public void UpdateProperty(String ref_no, String propType, String bedroom, Float rentprice, String furniture, String remark, String reporter_name, byte[] image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Property_Type, propType);
        cv.put(Bedroom, bedroom);
        cv.put(Rent_Price, rentprice);
        cv.put(Furniture, furniture);
        cv.put(Remark, remark);
        cv.put(Reporter_Name, reporter_name);
        cv.put(Image, image);

        int rowsAffected = db.update(Prop_Table, cv, "ref_num=?", new String[]{ref_no});

        if (rowsAffected > 0) {
            Log.d("DBUpdate", "Update successful for ref_num: " + ref_no);
        } else {
            Log.e("DBUpdate", "Update failed for ref_num: " + ref_no);
        }

        db.close();
    }

    //for authentication of user
    public class User {
        private int userId;
        private String username;

        public User(int userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }

    @SuppressLint("Range")
    public User getUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        User user = null;
        cursor = db.rawQuery("SELECT " + USER_ID + ", " + USERNAME + " FROM " + USER_TABLE + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?", new String[]{email, password});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(USER_ID));
            String username = cursor.getString(cursor.getColumnIndex(USERNAME));
            user = new User(userId, username);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return user;
    }

    @SuppressLint("Range")
    public UserModel getUserProfile(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        UserModel user = null;

        cursor = db.rawQuery(" SELECT * FROM " + USER_TABLE + " WHERE " + USER_ID + "=?", new String[]{id});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(USER_ID));
            String username = cursor.getString(cursor.getColumnIndex(USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(EMAIL));
            String phno = cursor.getString(cursor.getColumnIndex(PH_NO));
            String password = cursor.getString(cursor.getColumnIndex(PASSWORD));
            // Create a new UserModel object
            user = new UserModel(userId, username, email, phno, password);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return user;
    }

}
