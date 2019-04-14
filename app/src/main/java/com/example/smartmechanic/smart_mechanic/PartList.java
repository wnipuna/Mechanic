package com.example.smartmechanic.smart_mechanic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Interface.ItemClickListener;
import com.example.smartmechanic.smart_mechanic.ViewHolder.PartViewHolder;
import com.example.smartmechanic.smart_mechanic.Model.Part;
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

public class PartList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference partList;

    String categoryId="";

    FirebaseRecyclerAdapter<Part,PartViewHolder> adapter;

    FirebaseRecyclerAdapter<Part,PartViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        partList = database.getReference("Part");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_part);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get Instance here
        if(getIntent() !=null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId !=null){
            loadListPart(categoryId);
        }

        //Search
        materialSearchBar =(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter part name");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList){
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
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Part, PartViewHolder>(
                Part.class,
                R.layout.part,
                PartViewHolder.class,
                partList.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(PartViewHolder viewHolder, Part model, int position) {
                viewHolder.part_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.part_image);
                final Part local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent partDetails = new Intent(PartList.this,PartDetalis.class);
                        partDetails.putExtra("PartId",searchAdapter.getRef(position).getKey());
                        startActivity(partDetails);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        partList.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Part item = postSnapshot.getValue(Part.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListPart(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Part, PartViewHolder>(Part.class,R.layout.part,PartViewHolder.class,partList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(PartViewHolder viewHolder, Part model, int position) {
                viewHolder.part_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.part_image);
                final Part local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent partDetails = new Intent(PartList.this,PartDetalis.class);
                        partDetails.putExtra("PartId",adapter.getRef(position).getKey());
                        startActivity(partDetails);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }
}
