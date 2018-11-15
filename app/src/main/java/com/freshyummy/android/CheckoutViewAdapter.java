package com.freshyummy.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by afi on 09/04/18.
 */

public class CheckoutViewAdapter extends RecyclerView.Adapter<CheckoutViewAdapter.CheckoutViewHolder> {
    private Context mContext;
    private List<Cart> mListData;

    public CheckoutViewAdapter(Context mContext, List<Cart> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }
    @Override
    public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewAdapter.CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckoutViewHolder holder, int position) {
        final Cart dataProduct = mListData.get(position);
        Glide.with(mContext)
                .load(Utilities.getURLImageProduk()+dataProduct.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivProduct);
        holder.tvProductName.setText(dataProduct.getNamaproduk());
        holder.tvQuantity.setText("x "+dataProduct.getJumlahbeli());
        if (dataProduct.getDiscount().equals("0")) {
            holder.tvProductPriceDiscount.setText("");
            holder.tvProductPriceDiscount.setVisibility(View.INVISIBLE);
            holder.tvProductPriceUnit.setText(Utilities.getCurrency(dataProduct.getHarga())+"/"+dataProduct.getSatuan());
        } else {
            float xharga = 1 - (Float.parseFloat(dataProduct.getDiscount())/100);
            float hargadiscount = Float.parseFloat(dataProduct.getHarga()) / xharga;
            holder.tvProductPriceDiscount.setText(Utilities.getCurrency(String.valueOf(String.format("%.0f", hargadiscount))));
            holder.tvProductPriceDiscount.setVisibility(View.VISIBLE);
            holder.tvProductPriceUnit.setText("  "+Utilities.getCurrency(dataProduct.getHarga())+"/"+dataProduct.getSatuan());
        }
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName, tvProductPriceUnit, tvQuantity, tvProductPriceDiscount;
        public CheckoutViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_belanjaan);
            tvProductName = itemView.findViewById(R.id.tv_belanjaan_name);
            tvProductPriceUnit = itemView.findViewById(R.id.tv_belanjaan_price_unit);
            tvQuantity = itemView.findViewById(R.id.tv_belanjaan_quantity);
            tvProductPriceDiscount = itemView.findViewById(R.id.tv_belanjaan_price_unit_discount);

            Utilities.strikeThroughText(tvProductPriceDiscount);
        }
    }
}
