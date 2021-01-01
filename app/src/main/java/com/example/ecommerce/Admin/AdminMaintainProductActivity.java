package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.Seller.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {

    private TextView name,price,des;
    private Button applyChange,delbtn;
    private ImageView imageView;


    private String pId="";
    DatabaseReference prodRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);


        name=(TextView) findViewById(R.id.maintain_product_name);
        price=(TextView) findViewById(R.id.maintain_product_price);
        des=(TextView) findViewById(R.id.maintain_product_description);

        applyChange=(Button) findViewById(R.id.maintain_product_btn);
        delbtn=(Button) findViewById(R.id.maintain_product_btn_del);
        imageView=(ImageView) findViewById(R.id.maintain_product_image);

        pId=getIntent().getStringExtra("pid");

        prodRef= FirebaseDatabase.getInstance().getReference().child("Products").child(pId);

        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychangeMethod();
            }
        });

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
        
        DisplayProductInfo();
        
    }

    private void deleteThisProduct() {

        prodRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductActivity.this, "The Product is deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applychangeMethod() {

        String PName, PDescription, pPrice;

        PName = name.getText().toString();
        PDescription = des.getText().toString();
        pPrice = price.getText().toString();


        if (TextUtils.isEmpty(PName))
        {
            Toast.makeText(this, "Product name is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(PDescription))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", pId);
            productMap.put("description", PDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", PName);

            prodRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {



                    if (task.isSuccessful())
                    {

                        Toast.makeText(AdminMaintainProductActivity.this, "Changes applied successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(AdminMaintainProductActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void DisplayProductInfo() {
        prodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String pname=snapshot.child("pname").getValue().toString();
                    String pprice=snapshot.child("price").getValue().toString();
                    String pdes=snapshot.child("description").getValue().toString();
                    String pimg=snapshot.child("image").getValue().toString();

                    name.setText(pname);
                    price.setText(pprice);
                    des.setText(pdes);
                    Picasso.get().load(pimg).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}