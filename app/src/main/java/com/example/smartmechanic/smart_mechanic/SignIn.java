package com.example.smartmechanic.smart_mechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Common.Common;
import com.example.smartmechanic.smart_mechanic.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText editUsername,editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editUsername = (MaterialEditText)findViewById(R.id.editUsername);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //Init firebase

        FirebaseDatabase databae = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = databae.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(SignIn.this);
                mdialog.setMessage("Please waiting....");
                mdialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check if user exist in the database
                        if (dataSnapshot.child(editUsername.getText().toString()).exists()) {

                            //Get User Information
                            mdialog.dismiss();
                            User user = dataSnapshot.child(editUsername.getText().toString()).getValue(User.class);
                            user.setPhone(editUsername.getText().toString());
                            if (user.getPassword().equals(editPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Wrong Password or Phone number!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            mdialog.dismiss();
                            Toast.makeText(SignIn.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
