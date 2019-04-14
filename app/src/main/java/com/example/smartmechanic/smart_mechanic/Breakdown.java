package com.example.smartmechanic.smart_mechanic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
import com.example.smartmechanic.smart_mechanic.Model.BreakdownRequest;
import com.example.smartmechanic.smart_mechanic.Model.EmergencyRequest;
import com.example.smartmechanic.smart_mechanic.ViewHolder.BreakdownViewHolder;
import com.example.smartmechanic.smart_mechanic.ViewHolder.EmergencyRequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import info.hoang8f.widget.FButton;

public class Breakdown extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    RelativeLayout rootLayout;

    FirebaseDatabase db;
    DatabaseReference partList;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Breakdown,BreakdownViewHolder> adapter;

    MaterialEditText edtVehicleNumber,edtDescription,edtModel,edtLoaction,edtType,edtTransType,edtFuelType;
    FButton btnSelect,btnUpload;

    BreakdownRequest newRequest;
    Uri saveUri;


    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown);

        db = FirebaseDatabase.getInstance();
        partList = db.getReference("EBreakdowns");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);*/



        recyclerView = (RecyclerView)findViewById(R.id.listBreakdownRequest);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Breakdown.this);
        alertDialog.setTitle("Add new Request");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_request_layout = inflater.inflate(R.layout.add_new_breakdown_request,null);

        edtModel = add_request_layout.findViewById(R.id.editrModel);
        //edtType = add_request_layout.findViewById(R.id.editrType);
        //edtTransType = add_request_layout.findViewById(R.id.editrTransType);
        edtFuelType = add_request_layout.findViewById(R.id.editrFuelType);
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
                if(newRequest!=null){
                    try{
                        partList.push().setValue(newRequest);

                        String text = "New Breakdown Request Received!";
                        URL url = new URL("https://rest.nexmo.com/sms/json?from=NEXMO&to=94714653346&text="+ text +"&api_key=fed4172c&api_secret=2FofOCoyXdRnSDkU");
                        HttpsURLConnection myConnection =
                                (HttpsURLConnection) url.openConnection();
                        if (myConnection.getResponseCode() == 200) {

                        } else {
                            Toast.makeText(Breakdown.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){}
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
        try {

            if(saveUri !=null){
                final ProgressDialog mDialog = new ProgressDialog(this);
                mDialog.setMessage("Uploading");
                mDialog.show();

                String imageName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("image/"+imageName);
                imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDialog.dismiss();
                        Toast.makeText(Breakdown.this,"Uploaded",Toast.LENGTH_SHORT).show();
                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newRequest = new BreakdownRequest();
                                newRequest.setModel(edtModel.getText().toString());
                                newRequest.setVehicleNumber(edtVehicleNumber.getText().toString());
                                newRequest.setDescription(edtDescription.getText().toString());
                                newRequest.setLocation(edtLoaction.getText().toString());
                                //newRequest.setLocation("");
                                //newRequest.setType(edtType.getText().toString());
                                //newRequest.setTransType(edtTransType.getText().toString());
                                newRequest.setFueltype(edtFuelType.getText().toString());
                                newRequest.setImage(uri.toString());


                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDialog.dismiss();
                        Toast.makeText(Breakdown.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        mDialog.setMessage("Uploaded "+progress+"%");
                    }
                });

            }
        }
        catch (Exception e){
            Toast.makeText(Breakdown.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }
}
