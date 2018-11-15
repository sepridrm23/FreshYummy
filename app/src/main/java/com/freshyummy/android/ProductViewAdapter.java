package com.freshyummy.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afi on 17/03/18.
 */

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder> {
    private Context mContext;
    private List<Product> mListData;
    private String lastProduct="";

    public ProductViewAdapter(Context mContext, List<Product> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ProductViewAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product dataProduct = mListData.get(position);
//        if (!lastProduct.equals(dataProduct.getNamaproduk())) {
            Glide.with(mContext)
                    .load(Utilities.getURLImageProduk() + dataProduct.getGambar())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.ivProduct);
            holder.tvProductName.setText(dataProduct.getNamaproduk());
            holder.tvProductPrice.setText(Utilities.getCurrency(dataProduct.getHarga()) + "/" + dataProduct.getSatuan());
            if (dataProduct.getStatusketersediaan().equals("0")){
                holder.cardView.setAlpha(0.5f);
                holder.ivOutStock.setVisibility(View.VISIBLE);
            }else {
                holder.cardView.setAlpha(1);
                holder.ivOutStock.setVisibility(View.GONE);
            }

            if (dataProduct.getDiscount().equals("0")) {
                holder.cardViewDiscount.setVisibility(View.GONE);
                holder.tvPriceDiscount.setVisibility(View.GONE);
            } else {
                holder.tvDiscount.setText("diskon " + dataProduct.getDiscount() + "%");
                float xharga = 1 - (Float.parseFloat(dataProduct.getDiscount())/100);
                float hargadiscount = Float.parseFloat(dataProduct.getHarga()) / xharga;
                holder.tvPriceDiscount.setText(Utilities.getCurrency(String.valueOf(String.format("%.0f", hargadiscount))));
                holder.cardViewDiscount.setVisibility(View.VISIBLE);
                holder.tvPriceDiscount.setVisibility(View.VISIBLE);
            }
//        }else {
//            holder.cardView.setVisibility(View.GONE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> satuan = new ArrayList<>();
                for (int a=0; a<MainActivity.products_temp.size(); a++){
                    if (MainActivity.products_temp.get(a).getNamaproduk().equals(dataProduct.getNamaproduk())) {
                        satuan.add(new Product(MainActivity.products_temp.get(a).getIddetailproduk(), MainActivity.products_temp.get(a).getNamaproduk(),
                                MainActivity.products_temp.get(a).getKategori(), MainActivity.products_temp.get(a).getDeskripsi(),
                                MainActivity.products_temp.get(a).getSatuan(), MainActivity.products_temp.get(a).getHarga(),
                                MainActivity.products_temp.get(a).getGambar(), MainActivity.products_temp.get(a).getDiscount(),
                                MainActivity.products_temp.get(a).getStatusketersediaan()));
                    }
                }
                MainActivity.replaceFragment(FragmentProductDetail.newInstance(dataProduct.getGambar(), dataProduct.getNamaproduk()
                        , dataProduct.getHarga(), dataProduct.getKategori(), dataProduct.getDeskripsi(), dataProduct.getDiscount(), satuan), 5);

            }
        });

        lastProduct = dataProduct.getNamaproduk();
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivOutStock;
        TextView tvProductName, tvProductPrice, tvDiscount, tvPriceDiscount;
        CardView cardViewDiscount;
        LinearLayout cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.photo);
            ivOutStock = itemView.findViewById(R.id.photo_out);
            tvProductName = itemView.findViewById(R.id.name);
            tvDiscount = itemView.findViewById(R.id.discount);
            tvPriceDiscount = itemView.findViewById(R.id.price_discount);
            tvProductPrice = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.card_view);
            cardViewDiscount = itemView.findViewById(R.id.card_discount);

            Utilities.strikeThroughText(tvPriceDiscount);

            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            final DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            int viewPagerWidth = Math.round(outMetrics.widthPixels)/2;
            int more = viewPagerWidth/6;
            int viewPagerHeight = viewPagerWidth + more;

            cardView.setLayoutParams(new LinearLayout.LayoutParams(viewPagerWidth, viewPagerHeight));

        }
    }
}
