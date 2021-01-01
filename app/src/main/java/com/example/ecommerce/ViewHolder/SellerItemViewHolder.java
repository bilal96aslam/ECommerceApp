package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class SellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtState;
    public ImageView imageView;
    public ItemClickListner listner;

    public SellerItemViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtState = (TextView) itemView.findViewById(R.id.product_seller_state);
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }
}
