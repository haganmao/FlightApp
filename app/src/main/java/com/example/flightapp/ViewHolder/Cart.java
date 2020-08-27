package com.example.flightapp.ViewHolder;

import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flightapp.Class.GetCurrentUser;
import com.example.flightapp.Database.Database;
import com.example.flightapp.Model.Order;
import com.example.flightapp.Model.Request;
import com.example.flightapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice;
    Button btnConfirm;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Initial
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnConfirm = findViewById(R.id.btnBookTicket);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cart.size() > 0 ) {
                    showAlertDialog();
                }

                else
                    Toast.makeText(Cart.this, "You have not chose one ticket yet!!!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListTicket();

    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog .setTitle("One more Step");
        alertDialog.setMessage("Enter your Email address: ");

        final EditText edtEAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtEAddress.setLayoutParams(layoutParams);
        alertDialog.setView(edtEAddress);// Add edit Text to alert dialog
        alertDialog.setIcon(R.drawable.ic_my_bookings_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Request request = new Request(
                        GetCurrentUser.currentUser.getPhone(),
                        GetCurrentUser.currentUser.getName(),
                        edtEAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //Update to Firebase
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //Delete Cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you, Ticket Booked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                });

                alertDialog.show();

    }

    private void loadListTicket() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calculate total price

        int total = 0;

        for (Order order:cart)
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance();

        txtTotalPrice.setText(fmt.format(total));

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(GetCurrentUser.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //remove item at List<Order> by position

        cart.remove(position);

        //Then, delete all data from SQLite

        new Database(this).cleanCart();

        //Last, update new data from list<Order> to SQLite

        for (Order item:cart)
            new Database(this).addToCart(item);
        //Then
        loadListTicket();

    }
}
