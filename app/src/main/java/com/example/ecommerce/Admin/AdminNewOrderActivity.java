package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerView=findViewById(R.id.order_list);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(orderRef,AdminOrders.class)
                        .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int i, @NonNull final AdminOrders adminOrders) {

                        adminOrdersViewHolder.name.setText("Name: "+adminOrders.getName());
                        adminOrdersViewHolder.phone.setText("Phone: "+adminOrders.getPhone());
                        adminOrdersViewHolder.totalAmount.setText("Total Amount: $"+adminOrders.getTotalAmount());
                        adminOrdersViewHolder.dateTime.setText("Order at: "+adminOrders.getDate()+" "+adminOrders.getTime());
                        adminOrdersViewHolder.addressCity.setText("Address: "+adminOrders.getAddress()+" "+adminOrders.getCity());
                        adminOrdersViewHolder.show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String  Uid=getRef(i).getKey();
                                Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid",Uid);
                                startActivity(intent);
                            }
                        });
                        adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                                builder.setTitle("Have you shipped this order products ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0){

                                            String  Uid=getRef(i).getKey();
                                              RemoveOrder(Uid);

                                        }
                                        if(which==1){
                                           finish();
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        AdminOrdersViewHolder  adminOrdersViewHolder=new AdminOrdersViewHolder(view);
                        return adminOrdersViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void RemoveOrder(String uid) {

        orderRef.child(uid).removeValue();
    }


}