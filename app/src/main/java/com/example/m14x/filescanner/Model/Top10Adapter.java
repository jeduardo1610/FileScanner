package com.example.m14x.filescanner.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.m14x.filescanner.Controller.Top10ViewHolder;
import com.example.m14x.filescanner.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by m14x on 04/21/2016.
 */
public class Top10Adapter extends RecyclerView.Adapter<Top10ViewHolder> {
    private List<Integer> sizeCollection;
    private HashMap<String,Integer> fileDetail = new HashMap<String,Integer>();

    public Top10Adapter(List<Integer> sizeCollection,HashMap<String,Integer> fileDetail){
        this.sizeCollection = sizeCollection;
        this.fileDetail = fileDetail;
    }

    @Override
    public Top10ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtop10,parent,false);
        Top10ViewHolder viewHolder = new Top10ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Top10ViewHolder holder, int position) {

        String s = getKeyFromValue(fileDetail,sizeCollection.get(position));
        holder.setItem(s,sizeCollection.get(position),position);

    }

    @Override
    public int getItemCount() {
        if(sizeCollection.size()<10){
            return sizeCollection.size();
        }else {
            return sizeCollection.size();
        }
    }

    public static String getKeyFromValue(Map hm, Integer value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o.toString();
            }
        }
        return null;
    }
}
