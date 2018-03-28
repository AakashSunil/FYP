package com.example.aakash.cartmobile;

/**
 * Created by Aakash on 23-03-2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<ListItems> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, ArrayList<ListItems> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ListItems items = MainImageUploadInfoList.get(position);

        holder.ProductNameTextView.setText(items.getProductName());

        holder.ProductPriceTextView.setText(String.valueOf(items.getProductPrice()));

        holder.ProductQuantityTextView.setText(String.valueOf(items.getProductQuantity()));

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ProductNameTextView;
        public TextView ProductPriceTextView;
        public TextView ProductQuantityTextView;

        public ViewHolder(View itemView) {

            super(itemView);

            ProductNameTextView = (TextView) itemView.findViewById(R.id.ShowProductNameTextView);

            ProductPriceTextView = (TextView) itemView.findViewById(R.id.ShowProductPriceTextView);

            ProductQuantityTextView = (TextView) itemView.findViewById(R.id.ShowProductQuantityTextView);
        }
    }
}