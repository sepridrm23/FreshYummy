package com.freshyummy.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by afi on 26/04/18.
 */

public class FragmentAllPoin extends Fragment {
    User user;
    RecyclerView recyclerView;

    public FragmentAllPoin(){
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

        user = Utilities.getUser(getContext());

        if (PoinDetailActivity.pointDetails.size()!=0){
            rl.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            PoinDetailViewAdapter rcAdapter = new PoinDetailViewAdapter(getActivity(), PoinDetailActivity.pointDetails);
            recyclerView.setAdapter(rcAdapter);
        }else {
            rl.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
