package com.example.m14x.filescanner.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m14x.filescanner.Controller.FrecuentViewHolder;
import com.example.m14x.filescanner.Controller.Top10ViewHolder;
import com.example.m14x.filescanner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by m14x on 04/21/2016.
 */
public class FrecuentAdapter extends RecyclerView.Adapter<FrecuentViewHolder> {
    private Map<String,Integer> mExtentionFrec = new HashMap<>();
    private List<String> key = new ArrayList<>();

    public FrecuentAdapter(Map<String,Integer> mExtentionFrec){
        this.mExtentionFrec = mExtentionFrec;
    }

    @Override
    public FrecuentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfrecuent,parent,false);
        FrecuentViewHolder viewHolder = new FrecuentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FrecuentViewHolder holder, int position) {

        key = new ArrayList<String>(mExtentionFrec.keySet());

        holder.setItem(key.get(position),mExtentionFrec.get(key.get(position)),position);


    }

    @Override
    public int getItemCount() {
        if(mExtentionFrec.size()<5){
            return mExtentionFrec.size();
        }else {
            return 5;
        }
    }
}
