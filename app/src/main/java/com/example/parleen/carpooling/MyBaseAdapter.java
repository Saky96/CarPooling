package com.example.parleen.carpooling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by parleen on 7/28/2017.
 */

public class MyBaseAdapter extends BaseAdapter {
    Context ctx;
    List<PoolingDetails> poolingDetails1;
    List<Details> details1;

    public MyBaseAdapter(Context ctx, List<PoolingDetails> poolingDetails1) {
        this.ctx = ctx;
        this.poolingDetails1 = poolingDetails1;
        this.details1 = details1;

    }

    @Override
    public int getCount() {
        return poolingDetails1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.my_view, null);
        TextView textView = (TextView) view.findViewById(R.id.seats1);
        textView.setText(poolingDetails1.get(position).seatsAvailb + "");
        return view;
    }
}
