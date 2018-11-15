package com.freshyummy.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afi on 26/04/18.
 */

public class FragmentInPoin extends Fragment {
    List<PoinDetail> pointDetails = new ArrayList<>();
    RecyclerView recyclerView;

    public FragmentInPoin(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_poin_detail, container, false);
        recyclerView = v.findViewById(R.id.rv_poin);
        RelativeLayout rl = v.findViewById(R.id.rl);

        if (PoinDetailActivity.pointDetails.size()!=0){
            rl.setVisibility(View.GONE);
            pointDetails.clear();
            for (int a = 0; a < PoinDetailActivity.pointDetails.size(); a++){
                if (!PoinDetailActivity.pointDetails.get(a).getNominal().substring(0,1).equals("-")) {
                    pointDetails.add(new PoinDetail(PoinDetailActivity.pointDetails.get(a).getIddetailpoin(), PoinDetailActivity.pointDetails.get(a).getIdpelanggan(),
                            PoinDetailActivity.pointDetails.get(a).getTanggal(), PoinDetailActivity.pointDetails.get(a).getIdtransaksi(), PoinDetailActivity.pointDetails.get(a).getIdpromo(),
                            PoinDetailActivity.pointDetails.get(a).getNamaproduk(), PoinDetailActivity.pointDetails.get(a).getGambar(), PoinDetailActivity.pointDetails.get(a).getNominal(),
                            PoinDetailActivity.pointDetails.get(a).getStatustransaksi()));
                }
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            PoinDetailViewAdapter rcAdapter = new PoinDetailViewAdapter(getActivity(), pointDetails);
            recyclerView.setAdapter(rcAdapter);
        }else {
            rl.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
