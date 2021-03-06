package com.example.flightapp.ViewHolder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.flightapp.Class.GetCurrentUser;
import com.example.flightapp.Model.User;
import com.example.flightapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        //Init Paper

        Paper.init(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, Signup.class);
                startActivity(signUp);

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this, Signin.class);
                startActivity(signIn);
            }
        });


        //Check Remember User

        String user = Paper.book().read(GetCurrentUser.USER_KEY);
        String pwd = Paper.book().read(GetCurrentUser.PWD_KEY);

        if (user !=null && pwd != null)
        {
            if(!user.isEmpty() && !pwd.isEmpty())

                login(user,pwd);

        }
    }

    private void login(final String phone, final String pwd) {

        //init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (GetCurrentUser.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Loading time...Please waiting..");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //Check if user not exist in database
                    if (dataSnapshot.child(phone).exists()) {

                        //Get user information
//                            mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);

                        if (user.getPassword().equals(pwd)){
                            {
                                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                GetCurrentUser.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong password. Try again or check your Phone number ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                            mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User not in Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else{
            Toast.makeText(MainActivity.this, "Please check your connection..", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}
