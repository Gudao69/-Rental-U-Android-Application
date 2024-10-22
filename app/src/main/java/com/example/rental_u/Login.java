package com.example.rental_u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button btnLogin;
    EditText txtEmail,txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnLogin=findViewById(R.id.btnLogin);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (validate()) {

                    DBHelper.User user = dbHelper.getUser(email, password);

                    if (user != null) {
                        int userId = user.getUserId();
                        String username = user.getUsername();
                        Toast.makeText(getApplicationContext(), "Logged In Successfully" , Toast.LENGTH_SHORT).show();
                        Intent toView = new Intent(Login.this, Home.class);
                        toView.putExtra("USER_ID", userId);
                        toView.putExtra("USERNAME",username);
                        startActivity(toView);
                        txtEmail.setText("");
                        txtPassword.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Credentials Again!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill Email and password", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean validate() {

                String emailInput = txtEmail.getText().toString().trim();
                String passwordInput = txtPassword.getText().toString().trim();

                if(emailInput.isEmpty()) {
                    txtEmail.setError("Email is required");
                    txtEmail.requestFocus();
                    return false;
                }

                if(passwordInput.isEmpty()) {
                    txtPassword.setError("Password is required");
                    txtPassword.requestFocus();
                    return false;
                }
                return true;
            }
        });
    }
}