package com.freshyummy.android;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Date;
import java.util.List;

/**
 * Created by afi on 18/03/18.
 */

public class BelanjaanViewAdapter extends RecyclerView.Adapter<BelanjaanViewAdapter.BelanjaanViewHolder> {
    Context mContext;
    private List<Belanjaan> mListData;
    int totalbayar;

    public BelanjaanViewAdapter(Context mContext, List<Belanjaan> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public BelanjaanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_belanjaan, parent, false);
        return new BelanjaanViewAdapter.BelanjaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BelanjaanViewHolder holder, int position) {
        final Belanjaan dataBelanjaan = mListData.get(position);
        Glide.with(mContext)
                .load(Utilities.getURLImageProduk()+dataBelanjaan.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivProduct);
        if(dataBelanjaan.getStatustransaksi().equals("0")){
            holder.tvStatus.setText("Dipesan");
        }else if(dataBelanjaan.getStatustransaksi().equals("1")){
            holder.tvStatus.setText("Disiapkan");
        }else if(dataBelanjaan.getStatustransaksi().equals("2")){
            holder.tvStatus.setText("Diantar");
        }else if(dataBelanjaan.getStatustransaksi().equals("3")){
            holder.tvStatus.setText("Sampai");
        }else if(dataBelanjaan.getStatustransaksi().equals("4")){
            holder.tvStatus.setText("Dibatalkan");
        }

        totalbayar = 0;
        totalbayar = Integer.parseInt(dataBelanjaan.getTotalbayar());
        if (!dataBelanjaan.getNamapromo().equals("")) {
            if (dataBelanjaan.getJenispromo().equals("0")) {
                totalbayar = totalbayar + Integer.parseInt(dataBelanjaan.getOngkir());
                totalbayar = totalbayar - Integer.parseInt(dataBelanjaan.getNominal());
            }else {
                if (!dataBelanjaan.getNominal().equals("~")) {
                    int disc = Integer.parseInt(dataBelanjaan.getOngkir()) - Integer.parseInt(dataBelanjaan.getNominal());
                    if (disc < 0){
                        disc = 0;
                    }
                    totalbayar = totalbayar + disc;
                }
            }
        }else {
            totalbayar = totalbayar + Integer.parseInt(dataBelanjaan.getOngkir());
        }
        totalbayar = totalbayar - Integer.parseInt(dataBelanjaan.getPoinkeluar());
        if (totalbayar<0){
            totalbayar = 0;
        }

        String tanggal = dataBelanjaan.getTanggaltransaksi().substring(0, 10);
        holder.tvDate.setText(tanggal.substring(8, 10) + tanggal.substring(4, 8) + tanggal.substring(0, 4));
        holder.tvProductName.setText(dataBelanjaan.getNamaproduk());
        holder.tvTotalQuantity.setText(dataBelanjaan.getJumlahproduk()+" produk");
        holder.tvQuantity.setText("x "+dataBelanjaan.getJumlahbeli());
        holder.tvTotalBayar.setText(Utilities.getCurrency(String.valueOf(totalbayar)));
        holder.tvProductPriceUnit.setText(Utilities.getCurrency(dataBelanjaan.getHargabeli())+"/"+dataBelanjaan.getSatuan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailBelanjaanActivity.class);
                intent.putExtra("IDTRANSAKSI", dataBelanjaan.getIdtransaksi());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mContext).toBundle());
//                }else {
                    mContext.startActivity(intent);
//                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class BelanjaanViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName, tvProductPriceUnit, tvStatus, tvTotalBayar, tvQuantity, tvTotalQuantity, tvDate;
        public BelanjaanViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_belanjaan);
            tvProductName = itemView.findViewById(R.id.tv_belanjaan_name);
            tvProductPriceUnit = itemView.findViewById(R.id.tv_belanjaan_price_unit);
            tvStatus = itemView.findViewById(R.id.tv_item_order_history_last_status);
            tvTotalBayar = itemView.findViewById(R.id.tv_item_order_history_price);
            tvQuantity = itemView.findViewById(R.id.tv_belanjaan_quantity);
            tvTotalQuantity = itemView.findViewById(R.id.tv_item_order_history_unit);
            tvDate = itemView.findViewById(R.id.tv_item_order_history_date);
        }
    }
}
