package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class CartViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    public ItemClickListner listner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.cart_product_name);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.cart_product_quantity);
        txtProductPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
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
