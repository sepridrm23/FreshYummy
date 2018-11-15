package com.freshyummy.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by afi on 16/03/18.
 */

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    Context mContext;
    private List<Cart> mListData;

    public CartViewAdapter(Context mContext, List<Cart> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        return new CartViewAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, int position) {
        final Cart dataCart = mListData.get(position);

        Glide.with(mContext)
                .load(Utilities.getURLImageProduk()+dataCart.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivCart);
        holder.tvName.setText(dataCart.getNamaproduk());
        holder.tvPrice.setText(Utilities.getCurrency(dataCart.getHarga())+"/"+dataCart.getSatuan());
        holder.tvQuantity.setText(dataCart.getJumlahbeli());

        if (dataCart.getDiscount().equals("0")){
            holder.tvPriceDiscount.setVisibility(View.GONE);
        }else {
            holder.tvPriceDiscount.setVisibility(View.VISIBLE);
            float xharga = 1 - (Float.parseFloat(dataCart.getDiscount())/100);
            float hargadiscount = Float.parseFloat(dataCart.getHarga()) / xharga;
            holder.tvPriceDiscount.setText(Utilities.getCurrency(String.valueOf(String.format("%.0f", hargadiscount))));
        }

        if (dataCart.getStatusketersediaan().equals("0")){
            holder.cardView.setVisibility(View.VISIBLE);
            holder.relativeLayout.setAlpha(0.5f);
        }else {
            holder.cardView.setVisibility(View.GONE);
            holder.relativeLayout.setAlpha(1);
        }

        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataCart.getStatusketersediaan().equals("1")) {
                    int x = Integer.valueOf(holder.tvQuantity.getText().toString());
                    x++;
                    holder.tvQuantity.setText(String.valueOf(x));
                    DataHelper dbHelper = new DataHelper(mContext);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.onUpdate(db, dataCart.getIddetailproduk(), String.valueOf(x));
                    MainActivity.setCurrenBNV(1);
                }
            }
        });

        holder.ibMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataCart.getStatusketersediaan().equals("1")) {
                    int x = Integer.valueOf(holder.tvQuantity.getText().toString());
                    x--;
                    if (x <= 0) {
                        holder.tvQuantity.setText("0");
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(true);
                        builder.setTitle("Konfirmasi");
                        builder.setMessage("Hapus " + dataCart.getNamaproduk() + " dari keranjang ?");
                        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataHelper dbHelper = new DataHelper(mContext);
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                dbHelper.onDelete(db, dataCart.getIddetailproduk());
                                MainActivity.addBadgeCart(mContext);
                                MainActivity.setCurrenBNV(1);
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.tvQuantity.setText("1");
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        holder.tvQuantity.setText(String.valueOf(x));
                        DataHelper dbHelper = new DataHelper(mContext);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        dbHelper.onUpdate(db, dataCart.getIddetailproduk(), String.valueOf(x));
                        MainActivity.setCurrenBNV(1);
                    }
                }
            }
        });

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Hapus "+dataCart.getNamaproduk()+" dari keranjang ?");
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataHelper dbHelper = new DataHelper(mContext);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        dbHelper.onDelete(db, dataCart.getIddetailproduk());
                        MainActivity.addBadgeCart(mContext);
                        MainActivity.setCurrenBNV(1);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCart, ivDel;
        ImageButton ibPlus, ibMin;
        TextView tvName, tvPrice, tvPriceDiscount;
        EditText tvQuantity;
        ImageView cardView;
        RelativeLayout relativeLayout;
        public CartViewHolder(View itemView) {
            super(itemView);
            ivCart = itemView.findViewById(R.id.iv_item_cart_product);
            ivDel = itemView.findViewById(R.id.iv_item_cart_delete);
            ibPlus = itemView.findViewById(R.id.ib_item_cart_add_unit);
            ibMin = itemView.findViewById(R.id.ib_item_cart_remove_unit);
            tvName = itemView.findViewById(R.id.tv_item_cart_name);
            tvQuantity = itemView.findViewById(R.id.et_item_cart_unit);
            tvPrice = itemView.findViewById(R.id.tv_item_cart_price_unit);
            tvPriceDiscount = itemView.findViewById(R.id.tv_item_cart_price_unit_discount);
            cardView = itemView.findViewById(R.id.card_ready);
            relativeLayout = itemView.findViewById(R.id.relative);

            Utilities.strikeThroughText(tvPriceDiscount);
//            tvQuantity.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            tvQuantity.setTextColor(Color.DKGRAY);
        }
    }
}
