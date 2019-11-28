package com.example.lljsm.codeviewer;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CodeRecyclerViewAdapter extends RecyclerView.Adapter<CodeRecyclerViewAdapter.MyViewHolder>{
    private ArrayList<String> codes;
    private String color_background_odd;
    private String color_background_even;
    public CodeRecyclerViewAdapter(ArrayList<String> codes, String color_background_odd, String color_background_even){
        this.codes = new ArrayList<String>();
        int length = codes.size();
        for (int i = 0; i < length; i++) {
            this.codes.add(codes.get(i));
        }
        this.color_background_odd = color_background_odd;
        this.color_background_even = color_background_even;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_code_bar, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("222", codes.get(position));
        holder.code_view.setText(Html.fromHtml(codes.get(position), Html.FROM_HTML_MODE_COMPACT));
        holder.line_number.setText(String.valueOf(position + 1));
        if(position % 2 == 0){
            holder.main_item.setBackgroundColor(Color.parseColor(color_background_even));
        } else {
            holder.main_item.setBackgroundColor(Color.parseColor(color_background_odd));
        }
    }

    @Override
    public int getItemCount() {
        return (null == codes ? 0 : codes.size());
    }

    public void update_date_set(ArrayList<String> new_codes, String color_background_odd, String color_background_even){
        codes.clear();
        codes.addAll(new_codes);
        this.color_background_odd = color_background_odd;
        this.color_background_even = color_background_even;

        notifyDataSetChanged();
    }

    // ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView code_view;
        protected TextView line_number;
        protected LinearLayout main_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.line_number = (TextView) itemView.findViewById(R.id.textview_item_line_number);
            this.code_view = (TextView) itemView.findViewById(R.id.textview_item_code_context);
            this.main_item = (LinearLayout) itemView.findViewById(R.id.linearlayout_recycler_main_item);
        }
    }
}
