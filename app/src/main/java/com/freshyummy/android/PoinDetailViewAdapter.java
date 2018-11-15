package com.freshyummy.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class PoinDetailViewAdapter extends RecyclerView.Adapter<PoinDetailViewAdapter.PointViewHolder> {
    private Context mContext;
    private List<PoinDetail> mListDataPointDetail;

    public PoinDetailViewAdapter(Context mContext, List<PoinDetail> mListDataPointDetail) {
        this.mContext = mContext;
        this.mListDataPointDetail = mListDataPointDetail;
    }

    @Override
    public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_poin_detail, parent, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PointViewHolder holder, final int position) {
        final PoinDetail dataPointDetail = mListDataPointDetail.get(position);
        Glide.with(mContext)
                .load(Utilities.getURLImageProduk()+dataPointDetail.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPointThumbnail);

        if (dataPointDetail.getNominal().substring(0,1).equals("-")){
            holder.tvPointDescription.setText("Ditukar dengan Pembelian");
        }else {
            if (dataPointDetail.getStatustransaksi().equals("3")){
                holder.tvPointDescription.setText("Transaksi diselesaikan");
            }else {
                holder.tvPointDescription.setText("Transaksi dibatalkan");
            }
        }
        holder.tvPointTitle.setText(dataPointDetail.getNamaproduk());
        if (dataPointDetail.getNominal().substring(0,1).equals("-")){
            holder.tvPointValue.setText(dataPointDetail.getNominal());
        }else {
            holder.tvPointValue.setText("+"+dataPointDetail.getNominal());
        }
        holder.tvPointTimestamp.setText(dataPointDetail.getTanggal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataPointDetail.getIdtransaksi().equals("0")) {
                    Intent mIntent = new Intent(mContext, DetailBelanjaanActivity.class);
                    mIntent.putExtra("IDTRANSAKSI",
                            dataPointDetail.getIdtransaksi());
                    mContext.startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListDataPointDetail != null) ? mListDataPointDetail.size() : 0;
    }

    class PointViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPointThumbnail;
        TextView tvPointTimestamp, tvPointTitle, tvPointDescription, tvPointValue;

        PointViewHolder(View itemView) {
            super(itemView);
            ivPointThumbnail = itemView.findViewById(R.id.iv_point_thumbnail);
            tvPointTimestamp = itemView.findViewById(R.id.tv_point_timestamp);
            tvPointTitle = itemView.findViewById(R.id.tv_point_title);
            tvPointDescription = itemView.findViewById(R.id.tv_point_description);
            tvPointValue = itemView.findViewById(R.id.tv_point_value);
        }
    }
}