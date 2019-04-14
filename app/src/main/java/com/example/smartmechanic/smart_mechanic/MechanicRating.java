package com.example.smartmechanic.smart_mechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Common.Common;
import com.example.smartmechanic.smart_mechanic.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MechanicRating extends AppCompatActivity {

    EditText mechanicNumber;
    TextView mechanicName;
    EditText editRate;
    Button btnSubmit;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("User");
    final DatabaseReference table_rate = database.getReference("Rating");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        editRate = (EditText)findViewById(R.id.editRate);
        mechanicNumber = findViewById(R.id.editNumber);
        mechanicName = findViewById(R.id.textMechanic);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    final ProgressDialog mdialog = new ProgressDialog(MechanicRating.this);
                    mdialog.setMessage("Please waiting....");
                    mdialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check if user exist in the database
                            if (dataSnapshot.child(mechanicNumber.getText().toString()).exists()) {

                                //Get User Information
                                mdialog.dismiss();
                                User user = dataSnapshot.child(mechanicNumber.getText().toString()).getValue(User.class);
                                user.setPhone(mechanicNumber.getText().toString());

                                if (user.getType().equals("M".toString())) {
                                    mechanicName.setText(user.getName());
                                } else {
                                    user = null;
                                    Toast.makeText(MechanicRating.this, "Mechanic does not exist!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                mdialog.dismiss();
                                Toast.makeText(MechanicRating.this, "Mechanic does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //mdialog.dismiss();
                    //Snackbar.make(view, mechanicNumber.getText().toString(), Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
                }catch (Exception e){
                    Toast.makeText(MechanicRating.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mechanicName.getText().toString().equals("") || mechanicName.getText().toString().equals(null)) {
                    Toast.makeText(MechanicRating.this, "Please search a mechanic first", Toast.LENGTH_SHORT).show();
                }else{
                    final ProgressDialog mdialog = new ProgressDialog(MechanicRating.this);
                    mdialog.setMessage("Please waiting....");
                    mdialog.show();

                    mdialog.dismiss();
                    User user = new User(mechanicNumber.getText().toString(),mechanicName.getText().toString(),editRate.getText().toString(),"test","","","","");
                    table_rate.child(String.valueOf(java.util.UUID.randomUUID())).setValue(user);
                    Toast.makeText(MechanicRating.this, "Submitted Successfully!!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}
