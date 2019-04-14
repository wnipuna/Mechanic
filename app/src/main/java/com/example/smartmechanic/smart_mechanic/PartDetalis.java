package com.example.smartmechanic.smart_mechanic;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartmechanic.smart_mechanic.Database.Database;
import com.example.smartmechanic.smart_mechanic.Model.Order;
import com.example.smartmechanic.smart_mechanic.Model.Part;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PartDetalis extends AppCompatActivity {

    TextView part_name,part_price,part_description,part_model;
    ImageView part_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String partId="";
    Part currentpart;

    FirebaseDatabase database;
    DatabaseReference parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_detalis);

        database = FirebaseDatabase.getInstance();
        parts = database.getReference("Part");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        partId,
                        currentpart.getName(),
                       numberButton.getNumber(),
                        currentpart.getPrice(),
                        currentpart.getDiscount()

                ));

                Toast.makeText(PartDetalis.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        part_description = (TextView)findViewById(R.id.part_description);
        part_name = (TextView)findViewById(R.id.part_name);
        part_price = (TextView)findViewById(R.id.part_price);
        part_image = (ImageView)findViewById(R.id.img_part);
        part_model = (TextView)findViewById(R.id.part_model);

        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        if(getIntent() !=null)
            partId = getIntent().getStringExtra("PartId");
        if(!partId.isEmpty()){
            getDetailsPart(partId);
        }


    }

    private void getDetailsPart(String partId) {
        parts.child(partId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentpart = dataSnapshot.getValue(Part.class);

                Picasso.with(getBaseContext()).load(currentpart.getImage()).into(part_image);

                collapsingToolbarLayout.setTitle(currentpart.getName());

                part_price.setText(currentpart.getPrice());

                part_model.setText(currentpart.getModel());

                part_name.setText(currentpart.getName());

                part_description.setText(currentpart.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }
}
