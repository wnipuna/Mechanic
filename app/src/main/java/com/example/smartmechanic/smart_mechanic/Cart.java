package com.example.smartmechanic.smart_mechanic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartmechanic.smart_mechanic.Common.Common;
import com.example.smartmechanic.smart_mechanic.Database.Database;
import com.example.smartmechanic.smart_mechanic.Model.Order;
import com.example.smartmechanic.smart_mechanic.Model.Request;
import com.example.smartmechanic.smart_mechanic.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import javax.net.ssl.HttpsURLConnection;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton placeOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        placeOrder = (FButton)findViewById(R.id.btnPlaceOrder);

        placeOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loadListPart();
    }

    private void showAlertDialog() {

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter Your Address");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getName(),
                            edtAddress.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            cart,
                            date
                    );


                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    String body = "Hi! You have a cart of "+ txtTotalPrice.getText().toString() + " amount price. And it will delivered to " + edtAddress.getText().toString() + " address.";
                    URL url2 = new URL("https://emailsendapp.azurewebsites.net/api/HttpTriggerCSharp1?name="+Common.currentUser.getEmail()+"&message="+body+"&code=gbHLE/Vred/CRBE8NkXTwvWiVROEnZcUJGCI4JfAqkL0MMyPg26VbA==");
                    //URL url2 = new URL("https://emailsendapp.azurewebsites.net/api/HttpTriggerCSharp1?name=sasinduk@outlook.com&message="+body+"&code=gbHLE/Vred/CRBE8NkXTwvWiVROEnZcUJGCI4JfAqkL0MMyPg26VbA==");
                    HttpURLConnection client2 = (HttpURLConnection) url2.openConnection();
                    client2.setRequestMethod("POST");
                    //client2.setRequestProperty("Key","Value");
                    client2.setDoOutput(true);
                    /*OutputStream outputPost = new BufferedOutputStream(client2.getOutputStream());

                    String text2 = outputPost.toString();
                    outputPost.flush();
                    outputPost.close();*/

                    InputStream in;
                    in = client2.getInputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) > 0) {
                        System.out.println(new String(buffer, 0, read, "utf-8"));
                    }

                    String text = "Hi! Thank You, Order Placed.";
                    URL url = new URL("https://rest.nexmo.com/sms/json?from=NEXMO&to=94770260630&text="+ text +"&api_key=0f7c3f76c&api_secret=6LUsS4YIeJivSNKE"  );
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) url.openConnection();
                    if (myConnection.getResponseCode() == 200) {

                    } else {
                        Toast.makeText(Cart.this, "error", Toast.LENGTH_SHORT).show();
                    }
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this,"Thank You, Order Place",Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception e){
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this,"Thank You, Order Place",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListPart() {
        cart = new Database(this).getCart();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total =0;
        for(Order order:cart){

            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            Locale locale = new Locale("en","LK");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }
    }
}
