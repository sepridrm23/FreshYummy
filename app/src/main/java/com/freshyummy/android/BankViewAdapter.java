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
 * Created by afi on 11/04/18.
 */

public class BankViewAdapter extends RecyclerView.Adapter<BankViewAdapter.BankViewHolder> {
    Context mContext;
    private List<Bank> mListData;

    public BankViewAdapter(Context mContext, List<Bank> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank, parent, false);
        return new BankViewAdapter.BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankViewHolder holder, int position) {
        final Bank dataTransferBank = mListData.get(position);

        Glide.with(mContext)
                .load(Utilities.getURLImageBank()+dataTransferBank.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivTransferBankLogo);
        holder.tvTransferBankName.setText(dataTransferBank.getNamabank());
        holder.tvTransferBankAccountNumber.setText(dataTransferBank.getNorekening());
        holder.tvTransferBankAccountName.setText(dataTransferBank.getNamarekening());
//        holder.tvTransferBankCopyNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utilities.setClipboard(mContext, dataTransferBank.getNorekening());
//                Utilities.showAsToast(mContext,"Nomor Rekening telah disalin.");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class BankViewHolder extends RecyclerView.ViewHolder{
        ImageView ivTransferBankLogo;
        TextView tvTransferBankName, tvTransferBankAccountNumber, tvTransferBankAccountName;

        BankViewHolder(View itemView) {
            super(itemView);
            ivTransferBankLogo = itemView.findViewById(R.id.iv_item_transfer_bank);
            tvTransferBankName = itemView.findViewById(R.id.tv_item_transfer_name_bank);
            tvTransferBankAccountNumber = itemView.findViewById(R.id.tv_item_transfer_account_number);
            tvTransferBankAccountName = itemView.findViewById(R.id.tv_item_transfer_account_name);
        }
    }
}
