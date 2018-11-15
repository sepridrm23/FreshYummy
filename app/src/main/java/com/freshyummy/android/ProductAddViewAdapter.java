package com.freshyummy.android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afi on 17/03/18.
 */

public class ProductAddViewAdapter extends RecyclerView.Adapter<ProductAddViewAdapter.ProductAddViewHolder> {
    Context mContext;
    private List<Product> mListData;
    private String[] mString;

    public ProductAddViewAdapter(Context mContext, List<Product> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public ProductAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_add, parent, false);
        mString = new String[mListData.size()];
        return new ProductAddViewAdapter.ProductAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductAddViewHolder holder, int position) {
        final Product dataProduct = mListData.get(position);
        if (dataProduct.getDiscount().equals("0")){
            holder.tvUnitPriceDiscount.setVisibility(View.GONE);
        }else {
            float xharga = 1 - (Float.parseFloat(dataProduct.getDiscount())/100);
            float hargadiscount = Float.parseFloat(dataProduct.getHarga()) / xharga;
            holder.tvUnitPriceDiscount.setText(Utilities.getCurrency(String.valueOf(String.format("%.0f", hargadiscount))));
            holder.tvUnitPriceDiscount.setVisibility(View.VISIBLE);
        }

        DataHelper dbHelper = new DataHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart where iddetailproduk = '"+dataProduct.getIddetailproduk()+"'",null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            holder.etQuantity.setText(cursor.getString(2));
        }else {
            holder.etQuantity.setText("0");
        }

        holder.tvUnitPrice.setText(Utilities.getCurrency(dataProduct.getHarga())+"/"+dataProduct.getSatuan());
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.valueOf(holder.etQuantity.getText().toString());
                x++;
                holder.etQuantity.setText(String.valueOf(x));

                MainActivity.temp_carts.clear();
                for (int a=0; a<mListData.size(); a++) {
                    if (mString[a]==null){
                        mString[a] = "0";
                    }
                    if (mListData.get(a).getIddetailproduk().equals(dataProduct.getIddetailproduk())) {
                        mString[a]=String.valueOf(x);
                    }
                    MainActivity.temp_carts.add(new Cart("", mListData.get(a).getIddetailproduk(), "", "", "", "", mString[a], "", ""));
                }

//                DataHelper dbHelper = new DataHelper(mContext);
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Cursor cursor = db.rawQuery("SELECT * FROM cart where iddetailproduk = '"+dataProduct.getIddetailproduk()+"'",null);
//                if (cursor.getCount() == 0){
//                    dbHelper.onInsert(db, dataProduct.getIddetailproduk(), String.valueOf(x));
////                    Utilities.showAsToast(mContext, "Produk ditambah ke keranjang");
////                    MainActivity.addBadgeCart(mContext);
//                }else{
//                    dbHelper.onUpdate(db, dataProduct.getIddetailproduk(), String.valueOf(x));
////                    Utilities.showAsToast(mContext, "Produk ditambah ke keranjang");
////                    MainActivity.addBadgeCart(mContext);
//                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.valueOf(holder.etQuantity.getText().toString());
                x--;
                if(x<=0){
                    x=0;
                    holder.etQuantity.setText("0");
                }else {
                    holder.etQuantity.setText(String.valueOf(x));
                }

                MainActivity.temp_carts.clear();
                for (int a=0; a<mListData.size(); a++) {
                    if (mString[a]==null){
                        mString[a] = "0";
                    }
                    if (mListData.get(a).getIddetailproduk().equals(dataProduct.getIddetailproduk())) {
                        mString[a]=String.valueOf(x);
                    }
                    MainActivity.temp_carts.add(new Cart("", mListData.get(a).getIddetailproduk(), "", "", "", "", mString[a], "", ""));
                }

//                DataHelper dbHelper = new DataHelper(mContext);
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Cursor cursor = db.rawQuery("SELECT * FROM cart where iddetailproduk = '"+dataProduct.getIddetailproduk()+"'",null);
//                if (cursor.getCount() == 0){
//                    dbHelper.onInsert(db, dataProduct.getIddetailproduk(), String.valueOf(x));
////                    Utilities.showAsToast(mContext, "Produk ditambah ke keranjang");
////                    MainActivity.addBadgeCart(mContext);
//                }else{
//                    dbHelper.onUpdate(db, dataProduct.getIddetailproduk(), String.valueOf(x));
////                    Utilities.showAsToast(mContext, "Produk ditambah ke keranjang");
////                    MainActivity.addBadgeCart(mContext);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class ProductAddViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitPrice, tvUnitPriceDiscount;
        ImageButton btnPlus, btnMinus;
        EditText etQuantity;
        public ProductAddViewHolder(View itemView) {
            super(itemView);
            tvUnitPrice = itemView.findViewById(R.id.tv_item_product_price_unit);
            btnMinus = itemView.findViewById(R.id.ib_item_product_remove_unit);
            btnPlus = itemView.findViewById(R.id.ib_item_product_add_unit);
            etQuantity = itemView.findViewById(R.id.et_item_product_unit);
            tvUnitPriceDiscount = itemView.findViewById(R.id.tv_item_cart_price_unit_discount);
            Utilities.strikeThroughText(tvUnitPriceDiscount);
//            etQuantity.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            etQuantity.setTextColor(Color.DKGRAY);
        }
    }
}
