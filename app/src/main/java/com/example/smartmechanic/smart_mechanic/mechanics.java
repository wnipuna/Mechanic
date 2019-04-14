package com.example.smartmechanic.smart_mechanic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.smartmechanic.smart_mechanic.Model.BreakdownRequest;
import com.example.smartmechanic.smart_mechanic.Model.RatingRequest;
import com.example.smartmechanic.smart_mechanic.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class mechanics extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference req;
    FirebaseStorage storage;
    StorageReference storageReference;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<RatingRequest,OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics);

        database = FirebaseDatabase.getInstance();
        req = database.getReference("Rating");


        recyclerView = (RecyclerView)findViewById(R.id.listMechanics);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<RatingRequest, OrderViewHolder>(
                RatingRequest.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                req.orderByChild("username")
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder ViewHolder, RatingRequest model, int position) {
                ViewHolder.txtOrderId.setText("Name: "+ model.getPassword());
                ViewHolder.txtOrderStatus.setText("Number: "+ model.getUsername());
                ViewHolder.txtOrderAddress.setText("");
                ViewHolder.txtOrderPhone.setText("Rating(1-10): "+ model.getName());

            }
        };
        recyclerView.setAdapter(adapter);
    }
}
