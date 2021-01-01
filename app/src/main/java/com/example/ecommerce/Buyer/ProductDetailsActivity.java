package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.HomeActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

   // private com.google.android.material.floatingactionbutton.FloatingActionButton addToCart;
    private android.widget.ImageView product_img;
    private ElegantNumberButton elegantNumberButton;
    private android.widget.TextView prodPrice,Pname,Pdes;
    private String pId="",stat="Normal";
    private Button addcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

      // addToCart=(com.google.android.material.floatingactionbutton.FloatingActionButton)findViewById(R.id.fab_det);
        product_img=(ImageView) findViewById(R.id.product_image_details) ;
        elegantNumberButton=(ElegantNumberButton) findViewById(R.id.number_btn);

        prodPrice=(TextView) findViewById(R.id.product_price_details);
        Pname=(TextView) findViewById(R.id.product_name_details);
        Pdes=(TextView) findViewById(R.id.product_description_details);
        Pdes=(TextView) findViewById(R.id.product_description_details);
        addcart=(Button) findViewById(R.id.add_to_cart);

        pId=getIntent().getStringExtra("pid");


        getProductDetails(pId);

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stat.equals("Order Placed")||  stat.equals("Order Shipped") ){

                    Toast.makeText(ProductDetailsActivity.this,
                            " You can add an purchase more products, once your order is shipped or confirmed.",Toast.LENGTH_LONG).show();
                }
                else{
                    addTocartList();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStat();

    }

    private void addTocartList() {

        String saveCurrentDate,saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

      final DatabaseReference cartList= FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pid",pId);
        hashMap.put("pname",Pname.getText().toString());
        hashMap.put("price",prodPrice.getText().toString());
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("quantity",elegantNumberButton.getNumber());
        hashMap.put("discount","");
        //this for user cart
        cartList.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(pId).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //for admin
                            cartList.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(pId).updateChildren(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this,"Added to cart Successful!",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(String pId) {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");
        databaseReference.child(pId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products=snapshot.getValue(Products.class);
                    Pname.setText(products.getPname());
                    Pdes.setText(products.getDescription());
                    prodPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(product_img);
                }
                else{
                    Toast.makeText(ProductDetailsActivity.this,"Snapshot error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkOrderStat(){
        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String shipingStat=snapshot.child("stat").getValue().toString();


                    if(shipingStat.equals("shipped")){

                        stat="Order Shipped";
                    }
                    else if (shipingStat.equals("not shipped")){
                        stat="Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}