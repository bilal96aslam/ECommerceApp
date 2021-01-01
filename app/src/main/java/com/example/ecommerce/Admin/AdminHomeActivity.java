package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.HomeActivity;
import com.example.ecommerce.Buyer.MainActivity;
import com.example.ecommerce.R;

public class AdminHomeActivity extends AppCompatActivity {
    private Button checkOrder,logout,maintain,approve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        checkOrder = (Button) findViewById(R.id.check_order);
        logout = (Button) findViewById(R.id.Admin_logout_btn);
        maintain = (Button) findViewById(R.id.maintain_prod);
        approve = (Button) findViewById(R.id.approve_prod);

        checkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });

    }
}