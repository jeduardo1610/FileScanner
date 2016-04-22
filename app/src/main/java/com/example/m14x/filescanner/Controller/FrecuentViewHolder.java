package com.example.m14x.filescanner.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.m14x.filescanner.R;

/**
 * Created by m14x on 04/21/2016.
 */
public class FrecuentViewHolder extends RecyclerView.ViewHolder {

    private TextView fileExtention;
    private TextView occurences;


    public FrecuentViewHolder(View itemView) {

        super(itemView);
        fileExtention = (TextView) itemView.findViewById(R.id.sizeAverage);
        occurences = (TextView) itemView.findViewById(R.id.occurencesTextView);

    }


    public void setItem(String s, Integer integer,int pos) {
        fileExtention.setText(s);
        occurences.setText(Integer.toString(integer));

    }
}
