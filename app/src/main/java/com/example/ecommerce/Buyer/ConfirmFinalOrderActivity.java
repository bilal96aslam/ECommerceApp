package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.HomeActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText name,phone,address,city;
    private Button confirm;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        name=findViewById(R.id.shipment_name);
        phone=findViewById(R.id.shipment_number);
        address=findViewById(R.id.shipment_address);
        city=findViewById(R.id.shipment_city);
        confirm=(Button) findViewById(R.id.shipment_btn);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this,totalAmount,Toast.LENGTH_LONG).show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    
                validation();
            }
        });


    }

    private void validation() {
        if (TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this, "Please write your  full name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone.getText().toString()))
        {
            Toast.makeText(this, "Please write your  phone number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(address.getText().toString()))
        {
            Toast.makeText(this, "Please write your  full address...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(city.getText().toString()))
        {
            Toast.makeText(this, "Please write your  city name...", Toast.LENGTH_SHORT).show();
        }
        else{
            confirmOrder();
        }
    }

    private void confirmOrder() {
        String saveCurrentDate,saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        final HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("totalAmount",totalAmount);
        hashMap.put("name",name.getText().toString());
        hashMap.put("phone",phone.getText().toString());
        hashMap.put("address",address.getText().toString());
        hashMap.put("city",city.getText().toString());
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("stat","not shipped");

        orderRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"your final order has been placed successfully!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }

            }
        });

    }


}