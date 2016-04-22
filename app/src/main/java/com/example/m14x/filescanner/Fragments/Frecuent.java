package com.example.m14x.filescanner.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m14x.filescanner.Model.FrecuentAdapter;
import com.example.m14x.filescanner.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by m14x on 04/21/2016.
 */
public class Frecuent extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Map<String,Integer> map = new HashMap<>();


    @SuppressLint("ValidFragment")
    public Frecuent(Map<String,Integer> map){

        this.map = map;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout_frecuent,container,false);
        setRetainInstance(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.item_card);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FrecuentAdapter(map);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
