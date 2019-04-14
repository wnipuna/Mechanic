package com.example.smartmechanic.smart_mechanic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SignUp extends AppCompatActivity {

    MaterialEditText editUsername,editName,editAddress,editPassword,editNIC, editEmail, editVNumber;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editName = (MaterialEditText)findViewById(R.id.editName);
        editUsername = (MaterialEditText)findViewById(R.id.editUsername);
        editAddress = (MaterialEditText)findViewById(R.id.editAddress);
        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editNIC = (MaterialEditText)findViewById(R.id.editNIC);
        editEmail = (MaterialEditText)findViewById(R.id.editEmail);
        editVNumber = (MaterialEditText)findViewById(R.id.editVNumber);

        signUp = (Button)findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(SignUp.this);
                mdialog.setMessage("Please waiting....");
                mdialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            if (dataSnapshot.child(editUsername.getText().toString()).exists()){
                                mdialog.dismiss();
                                Toast.makeText(SignUp.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                mdialog.dismiss();
                                String regexNIC = "[0-9]{9}[x|X|v|V]$";
                                String regexEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

                                if(!(editNIC.getText().toString().matches(regexNIC)))
                                {
                                    Toast.makeText(SignUp.this, "NIC Number is Not Valid!", Toast.LENGTH_SHORT).show();

                                }
                                else if(!(editEmail.getText().toString().matches(regexEmail)))
                                {
                                    Toast.makeText(SignUp.this, "Email is Not Valid!", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    User user = new User(editUsername.getText().toString(),editPassword.getText().toString(),editName.getText().toString(),editAddress.getText().toString(),editNIC.getText().toString(), editEmail.getText().toString(), editVNumber.getText().toString(),"C");
                                    table_user.child(editUsername.getText().toString()).setValue(user);
                                    String text = "Hi! You have sucessfully registered as a user.";
                                    URL url = new URL("https://rest.nexmo.com/sms/json?from=NEXMO&to=94770260630&text="+ text +"&api_key=0f7c3f76c&api_secret=6LUsS4YIeJivSNKE");
                                    HttpsURLConnection myConnection =
                                            (HttpsURLConnection) url.openConnection();
                                    if (myConnection.getResponseCode() == 200) {

                                    } else {
                                        Toast.makeText(SignUp.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        }catch (Exception e){
                            Toast.makeText(SignUp.this, "Sign up Successfully!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
