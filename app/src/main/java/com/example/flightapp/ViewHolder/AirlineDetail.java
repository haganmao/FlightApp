package com.example.flightapp.ViewHolder;

import android.app.DownloadManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flightapp.Class.GetCurrentUser;
import com.example.flightapp.Database.Database;
import com.example.flightapp.Model.Order;
import com.example.flightapp.Model.Airline;
import com.example.flightapp.Model.Rating;
import com.example.flightapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AirlineDetail extends AppCompatActivity implements RatingDialogListener {

    TextView airline_name,airline_price,airline_description;

    ImageView airline_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton quantityButton;
    RatingBar ratingBar;

    String airlineId = "";

    FirebaseDatabase database;
    DatabaseReference airlines;
    DatabaseReference ratingsTbl;

    Airline currentAirline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airline_detail);

        //Firebase

        database = FirebaseDatabase.getInstance();
        airlines = database.getReference("Airlines");
        ratingsTbl = database.getReference("Rating");

        //Initial view

        quantityButton = findViewById(R.id.quantity_button);
        btnCart = findViewById(R.id.btnCart);
        btnRating = findViewById(R.id.btn_rating);
        ratingBar =findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        airlineId,
                        currentAirline.getName(),
                        quantityButton.getNumber(),
                        currentAirline.getPrice(),
                        currentAirline.getDiscount()


                ));

                Toast.makeText(AirlineDetail.this, "Added to My Bookings", Toast.LENGTH_SHORT).show();
            }
        });

        airline_name = findViewById(R.id.airlines_name);
        airline_price = findViewById(R.id.airline_price);
        airline_description = findViewById(R.id.airline_description);
        airline_image = findViewById(R.id.img_airline);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //Get Airline Id from activity
        if(getIntent() != null)
            airlineId = getIntent().getStringExtra("AirlinesId");

        if(!airlineId.isEmpty())
        {
            if (GetCurrentUser.isConnectedToInternet(getBaseContext())) {
                getDetailAirline(airlineId);
                getRatingAirline(airlineId);
            }
            else{

                Toast.makeText(AirlineDetail.this, "Please check your connection..", Toast.LENGTH_SHORT).show();
                return;

             }

        }

    }

    private void getRatingAirline(String airlineId) {

        com.google.firebase.database.Query airlineRating = ratingsTbl.orderByChild("airlineId").equalTo(airlineId);

        airlineRating.addValueEventListener(new ValueEventListener() {


            int count = 0, sum = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;

                }

                if(count != 0 )
                {
                    float average  = sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very bad","Not Good","Average","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this airline")
                .setDescription("Please rating your experience and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comments here..")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(AirlineDetail.this).show();

    }

    private void getDetailAirline(String airlineId) {
        airlines.child(this.airlineId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentAirline = dataSnapshot.getValue(Airline.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentAirline.getImage())
                        .into(airline_image);

                collapsingToolbarLayout.setTitle(currentAirline.getName());
                airline_price.setText(currentAirline.getPrice());
                airline_name.setText(currentAirline.getName());
                airline_description.setText(currentAirline.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {
        //Get rating and upload to firebase

         final Rating rating = new Rating(GetCurrentUser.currentUser.getPhone(),
                airlineId,
                String.valueOf(value),
                comments);

        ratingsTbl.child(GetCurrentUser.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(GetCurrentUser.currentUser.getPhone()).exists())
                {
                    //Remove old value

                    ratingsTbl.child(GetCurrentUser.currentUser.getPhone()).removeValue();

                    //Update new Value
                    ratingsTbl.child(GetCurrentUser.currentUser.getPhone()).setValue(rating);
                }
                else
                    {
                        ratingsTbl.child(GetCurrentUser.currentUser.getPhone()).setValue(rating);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
