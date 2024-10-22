package com.example.rental_u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    Button register;

    EditText username,email,phone,password,cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        register=findViewById(R.id.btnRegister);
        username = findViewById(R.id.txtUsername);
        email =findViewById(R.id.txtEmail);
        phone = findViewById(R.id.txtPhno);
        password = findViewById(R.id.txtPassword);
        cpassword=findViewById(R.id.txtConPass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    if(password.getText().toString().equals(cpassword.getText().toString()))
                    {
                        String s_name = username.getText().toString();
                        String s_email = email.getText().toString();
                        String s_phone = phone.getText().toString();
                        String s_pass = password.getText().toString();

                        DBHelper register = new DBHelper(getApplicationContext());

                        if(register.isUsernameExists(s_name))
                        {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                        else if (register.isEmailExists(s_email))
                        {
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                        else if (register.isPhoneNumberExists(s_phone))
                        {
                            Toast.makeText(getApplicationContext(), "Phone number already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            register.addUser(s_name,s_email,s_phone,s_pass);
                            Toast.makeText(getApplicationContext(),"Registered successfully",Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Register.this, Login.class);
                            startActivity(i);
                            finish();
                        };
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Check Your Passwords Again",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean validate() {
                String usernameInput = username.getText().toString().trim();
                String emailInput = email.getText().toString().trim();
                String phoneInput = phone.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                String cpasswordInput = cpassword.getText().toString().trim();

                if(usernameInput.isEmpty()) {
                    username.setError("Name is required");
                    username.requestFocus();
                    return false;
                }

                if(emailInput.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return false;
                }

                if(!isValidEmail(emailInput)) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                    return false;
                }

                if(phoneInput.isEmpty()) {
                    phone.setError("Phone number is required");
                    phone.requestFocus();
                    return false;
                }

                if(!isValidPhoneNumber(phoneInput)) {
                    phone.setError("Please enter a valid email");
                    phone.requestFocus();
                    return false;
                }

                if(passwordInput.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return false;
                }

                if(cpasswordInput.isEmpty()) {
                    cpassword.setError("Confirm password is required");
                    cpassword.requestFocus();
                    return false;
                }

                return true;
            }

            private boolean isValidEmail(String email) {
                String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
                return Pattern.compile(emailRegex).matcher(email).matches();
            }
            private boolean isValidPhoneNumber(String phoneNumber) {
                String regex = "^[0-9]+$";
                return phoneNumber.matches(regex);
            }

        });
    }
}