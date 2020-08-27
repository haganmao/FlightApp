package com.example.flightapp.ViewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.flightapp.Class.GetCurrentUser;
import com.example.flightapp.Database.Database;
import com.example.flightapp.Model.Airline;
import com.example.flightapp.Interface.ItemClickListener;
import com.example.flightapp.R;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AirlinesList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference airlineList;

    String categoryId ="";
    FirebaseRecyclerAdapter<Airline, AirlineViewHolder> adapter;


    //Search function

    FirebaseRecyclerAdapter<Airline,AirlineViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airlines_list);


        //Firebase
        database = FirebaseDatabase.getInstance();
        airlineList = database.getReference("Airlines");


        //localDB

        localDB = new Database(this);

        recyclerView = findViewById(R.id.recycler_airlines);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent
        if ( getIntent() != null )
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null ){

                if(GetCurrentUser.isConnectedToInternet(getBaseContext()))
                    loadAirlineList(categoryId);
                else
                    {

                        Toast.makeText(AirlinesList.this, "Please check your connection..", Toast.LENGTH_SHORT).show();
                        return;
                    }
        }

        //Search

        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search your Ticket");

        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user enter key words, then will change suggest list


                List<String> suggest = new ArrayList<String>();
                    for(String search:suggestList )//loop in suggest list
                    {
                        if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }

                    materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //WHEN SEARCH BAR IS CLOSE RESTORE ORIGINAL SUGGESTION ADAPTER

                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    //when search finish show the final result
                    startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Airline, AirlineViewHolder>(
                Airline.class,
                R.layout.airlines_route,
                AirlineViewHolder.class,
                airlineList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(AirlineViewHolder viewHolder, Airline model, int position) {
                viewHolder.airline_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.airline_image);

                final Airline local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //show the airline name
                        //Toast.makeText(AirlinesList.this,"" + local.getName(),Toast.LENGTH_SHORT).show();

                        //Start new Activity which is details of airlines
                        Intent airlineDetail = new Intent(AirlinesList.this, AirlineDetail.class);
                        //Send Airline ID to new Activity
                        airlineDetail.putExtra("AirlinesId",searchAdapter.getRef(position).getKey());
                        startActivity(airlineDetail);


                    }
                });

            }
        };
        recyclerView.setAdapter(searchAdapter);

    }

    private void loadSuggest() {
        airlineList.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                         for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                         {

                             Airline item = postSnapshot.getValue(Airline.class);
                             suggestList.add(item.getName());//add name of airline to suggest list

                         }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadAirlineList(String categoryId) {
        //select airline from airlineList where CategoryId = categoryId
        adapter = new FirebaseRecyclerAdapter<Airline, AirlineViewHolder>(Airline.class,
                R.layout.airlines_route,
                AirlineViewHolder.class,
                airlineList.orderByChild("CategoryId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final AirlineViewHolder viewHolder, final Airline model, final int position) {
                viewHolder.airline_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.airline_image);


                //Add Favorites
                if(localDB.isFav(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                //Click to change

                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!localDB.isFav(adapter.getRef(position).getKey()))
                        {
                            localDB.addToFav(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(AirlinesList.this, "" + model.getName() + " was added to your favorite airlines", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            localDB.removeFromFav(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(AirlinesList.this, "" + model.getName() + " was removed from your favorite airlines", Toast.LENGTH_SHORT).show();

                        }
                    }
                });



                final Airline local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //show the airline name
                        //Toast.makeText(AirlinesList.this,"" + local.getName(),Toast.LENGTH_SHORT).show();

                        //Start new Activity which is details of airlines
                        Intent airlineDetail = new Intent(AirlinesList.this, AirlineDetail.class);
                        //Send Airline ID to new Activity
                        airlineDetail.putExtra("AirlinesId",adapter.getRef(position).getKey());
                        startActivity(airlineDetail);


                    }
                });
            }
        };

        //set adapter
        recyclerView.setAdapter(adapter);
    }

}
