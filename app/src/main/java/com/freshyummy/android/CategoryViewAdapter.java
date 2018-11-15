package com.freshyummy.android;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

import java.util.List;

/**
 * Created by afi on 15/03/18.
 */

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder> {
    Context mContext;
    private List<Category> mListData;

    public CategoryViewAdapter(Context mContext, List<Category> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Category dataCategory = mListData.get(position);

        Glide.with(mContext)
                .load(Utilities.getURLImageKategori()+dataCategory.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivCategory);
        holder.tvCategory.setText(dataCategory.getNamakategori());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.replaceFragment(FragmentProduct.newInstance(dataCategory.getNamakategori()), 4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListData != null) ? mListData.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategory;
        TextView tvCategory;
        RelativeLayout cardView;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.photo);
            tvCategory = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.card_view);

            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            final DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            int viewPagerWidth = Math.round(outMetrics.widthPixels)/4;
            int viewPagerHeight = viewPagerWidth + (viewPagerWidth/4);

            cardView.setLayoutParams(new LinearLayout.LayoutParams(viewPagerWidth, viewPagerHeight));
        }
    }
}
