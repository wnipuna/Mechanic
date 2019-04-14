package com.example.smartmechanic.smart_mechanic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Common.Common;
import com.example.smartmechanic.smart_mechanic.Interface.ItemClickListener;
import com.example.smartmechanic.smart_mechanic.Model.EmergencyRequest;
import com.example.smartmechanic.smart_mechanic.Model.User;
import com.example.smartmechanic.smart_mechanic.ViewHolder.EmergencyRequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import info.hoang8f.widget.FButton;

public class Emergency extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    RelativeLayout rootLayout;

    FirebaseDatabase db;
    DatabaseReference partList;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<EmergencyRequest,EmergencyRequestViewHolder> adapter;

    MaterialEditText edtVehicleNumber,edtDescription,edtModel,edtLoaction;
    FButton btnSelect,btnUpload;

    EmergencyRequest newRequest;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        db = FirebaseDatabase.getInstance();
        partList = db.getReference("ERequests");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = (RecyclerView)findViewById(R.id.listEmergencyRequest);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPartDialog();
            }
        });


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void showAddPartDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Emergency.this);
        alertDialog.setTitle("Add new Request");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_request_layout = inflater.inflate(R.layout.add_new_emergency_request,null);

        edtModel = add_request_layout.findViewById(R.id.editrModel);
        edtDescription = add_request_layout.findViewById(R.id.editrDescription);
        edtVehicleNumber = add_request_layout.findViewById(R.id.editrNumber);
        //edtLoaction = add_request_layout.findViewById(R.id.editrAddress);

        btnSelect =add_request_layout.findViewById(R.id.btnrSelect);
        btnUpload =add_request_layout.findViewById(R.id.btnrUpload);

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_request_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //Toast.makeText(Emergency.this, "1", Toast.LENGTH_SHORT).show();
                if(newRequest!=null){
                    //partList.push().setValue(newRequest);
                    //Toast.makeText(Emergency.this, "2", Toast.LENGTH_SHORT).show();
                    partList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try{
                                partList.child(edtModel.getText().toString()).setValue(newRequest);


                                String text = "New Emergency Request Received!";
                                URL url = new URL("https://rest.nexmo.com/sms/json?from=NEXMO&to=94714653346&text="+ text +"&api_key=fed4172c&api_secret=2FofOCoyXdRnSDkU");
                                HttpsURLConnection myConnection =
                                        (HttpsURLConnection) url.openConnection();
                                if (myConnection.getResponseCode() == 200) {

                                } else {
                                    Toast.makeText(Emergency.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                String ex = e.getMessage();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Snackbar.make(rootLayout,databaseError.getMessage().toString(),Snackbar.LENGTH_SHORT).show();
                        }
                    });


                    Snackbar.make(rootLayout,"Request was sent",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void uploadImage() {
        if(saveUri !=null){
            try {
                final ProgressDialog mDialog = new ProgressDialog(this);
                mDialog.setMessage("Uploading");
                mDialog.show();
                //Toast.makeText(Emergency.this, "11", Toast.LENGTH_SHORT).show();

                String imageName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("image/"+imageName);
                //Toast.makeText(Emergency.this, imageName, Toast.LENGTH_SHORT).show();

                imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDialog.dismiss();
                        Toast.makeText(Emergency.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newRequest = new EmergencyRequest();
                                newRequest.setModel(edtModel.getText().toString());
                                newRequest.setVehicleNumber(edtVehicleNumber.getText().toString());
                                newRequest.setDescription(edtDescription.getText().toString());
                                //newRequest.setLocation(edtLoaction.getText().toString());
                                newRequest.setLocation("");
                                newRequest.setImage(uri.toString());


                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDialog.dismiss();
                        Toast.makeText(Emergency.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        mDialog.setMessage("Uploaded "+progress+"%");
                    }
                });
            }
            catch (Exception e)
            {
                Toast.makeText(Emergency.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }


        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"), Common.PICK_IMAGE_REQUEST);
    }

    /*private void loadRequestList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<EmergencyRequest,EmergencyRequestViewHolder>(
                EmergencyRequest.class,
                R.layout.emerge_layout,
                EmergencyRequestViewHolder.class
        ) {
            @Override
            protected void populateViewHolder(EmergencyRequestViewHolder viewHolder, EmergencyRequest model, int position) {
                viewHolder.edtVehicleNumber.setText(model.getModel());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }

}
