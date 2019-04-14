package com.example.smartmechanic.smart_mechanic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Common.Common;
import com.example.smartmechanic.smart_mechanic.Model.Request;
import com.example.smartmechanic.smart_mechanic.Model.User;
import com.example.smartmechanic.smart_mechanic.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstatus);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() == null){
            loadOrders(Common.currentUser.getPhone());
        }
        else{
            loadOrders(getIntent().getStringExtra("Phone"));
        }



    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("Phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder ViewHolder, Request model, int position) {
                ViewHolder.txtOrderId.setText("Order Id: " + adapter.getRef(position).getKey());
                ViewHolder.txtOrderStatus.setText("Order Status: " + Common.convertCodeToStatus(model.getStatus()));
                ViewHolder.txtOrderAddress.setText("Order Address: " + model.getAddress());
                ViewHolder.txtOrderPhone.setText("Order Phone: " + model.getPhone());

            }
        };
        recyclerView.setAdapter(adapter);
    }

    /*public static void findUserById(@NonNegative int id, Listener listener) {
        FirebaseDatabase.getInstance().getreference("user")
                .orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        user = dataSnapshot.getValue(User.class);
                        listener.onUserRetrieved(user);
                    }
                ...
                });
    }*/


}
