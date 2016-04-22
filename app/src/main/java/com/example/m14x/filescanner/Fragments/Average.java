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
import android.widget.TextView;

import com.example.m14x.filescanner.R;

import java.util.List;

/**
 * Created by m14x on 04/21/2016.
 */
public class Average extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int avg;
    private TextView avgTxt;

    @SuppressLint("ValidFragment")
    public Average(int avg){
        this.avg = avg;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout_average,container,false);
        avgTxt = (TextView) view.findViewById(R.id.sizeAverage);
        setRetainInstance(true);
        avgTxt.setText(Integer.toString(avg));


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
