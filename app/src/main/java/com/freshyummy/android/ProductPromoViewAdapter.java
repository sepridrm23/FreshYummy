package com.freshyummy.android;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afi on 15/03/18.
 */

public class ProductPromoViewAdapter extends RecyclerView.Adapter<ProductPromoViewAdapter.MostViewHolder> {
    private Context mContext;
    private List<Product> mListData;
    private int flag;

    public ProductPromoViewAdapter(Context mContext, List<Product> mListData, int flag) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.flag = flag;
    }

    @Override
    public MostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_promo, parent, false);
        return new ProductPromoViewAdapter.MostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MostViewHolder holder, int position) {
        final Product dataProduct = mListData.get(position);

        Glide.with(mContext)
                .load(Utilities.getURLImageProduk() + dataProduct.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivProduct);
        holder.tvProductName.setText(dataProduct.getNamaproduk());
        holder.tvProductPrice.setText(Utilities.getCurrency(dataProduct.getHarga()) + "/" + dataProduct.getSatuan());
        if (dataProduct.getStatusketersediaan().equals("0")){
            holder.linearLayout.setAlpha(0.5f);
            holder.ivStock.setVisibility(View.VISIBLE);
        }else {
            holder.linearLayout.setAlpha(1);
            holder.ivStock.setVisibility(View.GONE);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> satuan = new ArrayList<>();
                if (flag==1){
                    for (int a=0; a<MainActivity.products_temp.size(); a++){
                        if (MainActivity.products_temp.get(a).getNamaproduk().equals(dataProduct.getNamaproduk())) {
                            satuan.add(new Product(MainActivity.products_temp.get(a).getIddetailproduk(), MainActivity.products_temp.get(a).getNamaproduk(),
                                    MainActivity.products_temp.get(a).getKategori(), MainActivity.products_temp.get(a).getDeskripsi(),
                                    MainActivity.products_temp.get(a).getSatuan(), MainActivity.products_temp.get(a).getHarga(),
                                    MainActivity.products_temp.get(a).getGambar(), MainActivity.products_temp.get(a).getDiscount(),
                                    MainActivity.products_temp.get(a).getStatusketersediaan()));
                        }
                    }
                }else if (flag==2) {
                    for (int a = 0; a < MainActivity.products_discount.size(); a++) {
                        if (MainActivity.products_discount.get(a).getNamaproduk().equals(dataProduct.getNamaproduk())) {
                            satuan.add(new Product(MainActivity.products_discount.get(a).getIddetailproduk(),
                                    MainActivity.products_discount.get(a).getNamaproduk(),
                                    MainActivity.products_discount.get(a).getKategori(),
                                    MainActivity.products_discount.get(a).getDeskripsi(),
                                    MainActivity.products_discount.get(a).getSatuan(),
                                    MainActivity.products_discount.get(a).getHarga(),
                                    MainActivity.products_discount.get(a).getGambar(),
                                    MainActivity.products_discount.get(a).getDiscount(),
                                    MainActivity.products_discount.get(a).getStatusketersediaan()));
                        }
                    }
                }
                else if (flag==3){
                    for (int a = 0; a < MainActivity.products_newest.size(); a++) {
                        if (MainActivity.products_newest.get(a).getNamaproduk().equals(dataProduct.getNamaproduk())) {
                            satuan.add(new Product(MainActivity.products_newest.get(a).getIddetailproduk(),
                                    MainActivity.products_newest.get(a).getNamaproduk(),
                                    MainActivity.products_newest.get(a).getKategori(),
                                    MainActivity.products_newest.get(a).getDeskripsi(),
                                    MainActivity.products_newest.get(a).getSatuan(),
                                    MainActivity.products_newest.get(a).getHarga(),
                                    MainActivity.products_newest.get(a).getGambar(),
                                    MainActivity.products_newest.get(a).getDiscount(),
                                    MainActivity.products_newest.get(a).getStatusketersediaan()));
                        }
                    }
                }else {
                    for (int a = 0; a < MainActivity.products_other.size(); a++) {
                        if (MainActivity.products_other.get(a).getNamaproduk().equals(dataProduct.getNamaproduk())) {
                            satuan.add(new Product(MainActivity.products_other.get(a).getIddetailproduk(),
                                    MainActivity.products_other.get(a).getNamaproduk(),
                                    MainActivity.products_other.get(a).getKategori(),
                                    MainActivity.products_other.get(a).getDeskripsi(),
                                    MainActivity.products_other.get(a).getSatuan(),
                                    MainActivity.products_other.get(a).getHarga(),
                                    MainActivity.products_other.get(a).getGambar(),
                                    MainActivity.products_other.get(a).getDiscount(),
                                    MainActivity.products_other.get(a).getStatusketersediaan()));
                        }
                    }
                }
                MainActivity.replaceFragment(FragmentProductDetail.newInstance(dataProduct.getGambar(), dataProduct.getNamaproduk()
                        , dataProduct.getHarga(), dataProduct.getKategori(), dataProduct.getDeskripsi(), dataProduct.getDiscount(), satuan), 4);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class MostViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivStock;
        TextView tvProductName, tvProductPrice, tvDiscount, tvPriceDiscount;
        CardView cardViewDiscount;
        LinearLayout linearLayout;
        public MostViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_item_product);
            ivStock = itemView.findViewById(R.id.photo_out);
            tvProductName = itemView.findViewById(R.id.tv_item_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_item_product_price);
            tvDiscount = itemView.findViewById(R.id.discount);
            tvPriceDiscount = itemView.findViewById(R.id.price_discount);
            cardViewDiscount = itemView.findViewById(R.id.card_discount);
            linearLayout = itemView.findViewById(R.id.linearLayout);

            Utilities.strikeThroughText(tvPriceDiscount);

            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            final DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            int viewPagerWidth = Math.round(outMetrics.widthPixels)/4;
//            int more = viewPagerWidth/3;
//            int viewPagerHeight = viewPagerWidth + more;

            ivProduct.setLayoutParams(new RelativeLayout.LayoutParams(viewPagerWidth, viewPagerWidth));
        }
    }
}
