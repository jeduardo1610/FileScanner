package com.example.m14x.filescanner.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.m14x.filescanner.R;

import org.w3c.dom.Text;

import java.util.Map;

/**
 * Created by m14x on 04/21/2016.
 */
public class Top10ViewHolder extends RecyclerView.ViewHolder {
    private TextView nameFile;
    private TextView sizeFile;
    private TextView rateFile;

    public Top10ViewHolder(View itemView) {
        super(itemView);
        nameFile = (TextView) itemView.findViewById(R.id.fileNameTextView);
        sizeFile = (TextView) itemView.findViewById(R.id.fileSizeTextView);
        rateFile = (TextView) itemView.findViewById(R.id.rateTextView);
    }


    public void setItem(String s, Integer i,int pos){
        nameFile.setText(s);
        sizeFile.setText(Integer.toString(i/1024)+ "KB");
        rateFile.setText(Integer.toString(pos+1));
        if(pos > 10){
            rateFile.setTextSize(13);
        }
        rateFile.setTextSize(25-(pos+2));
    }



}
