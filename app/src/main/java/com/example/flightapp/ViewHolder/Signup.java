package com.example.flightapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flightapp.Class.GetCurrentUser;
import com.example.flightapp.Model.User;
import com.example.flightapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    EditText edtPhone,edtName,edtPassword,edtSecureCode;
    Button btnSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtSecureCode = findViewById(R.id.edtSecureCode);

        btnSignUp = findViewById(R.id.btnSignUp);

        //init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GetCurrentUser.isConnectedToInternet(getBaseContext())){

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check the phone exist or not
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()){

                            Toast.makeText(Signup.this,"Phone Number already exists",Toast.LENGTH_SHORT).show();
                        }
                        else{

                            User user = new User(edtName.getText().toString(),
                                    edtPassword.getText().toString(),
                                    edtSecureCode.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(Signup.this,"Sign up successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else
                {
                    Toast.makeText(Signup.this, "Please check your connection..", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
