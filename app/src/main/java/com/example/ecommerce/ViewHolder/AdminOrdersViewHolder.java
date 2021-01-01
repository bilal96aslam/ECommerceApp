package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name, addressCity,
            phone, totalAmount,dateTime;
    public Button show;

    public ItemClickListner listner;

    public AdminOrdersViewHolder(@NonNull View itemView) {

        super(itemView);
        name=itemView.findViewById(R.id.order_user_name);
        addressCity=itemView.findViewById(R.id.order_address_city);
        phone=itemView.findViewById(R.id.order_number);
        totalAmount=itemView.findViewById(R.id.order_price);
        dateTime=itemView.findViewById(R.id.order_date_time);
        show=itemView.findViewById(R.id.show_prod_btn);


    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
