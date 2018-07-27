package com.example.home.trying;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fci on 16/01/18.
 */

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.ViewHolder> {
    private List<history_row_item> history_row_items;
    private Context context;


    History_Adapter(List<history_row_item> history_row, Context context) {
        this.history_row_items = history_row;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final history_row_item blood_noti = history_row_items.get(position);

        holder.item_.setText(blood_noti.getItem());

    }

    @Override
    public int getItemCount() {
        return history_row_items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_;
        ViewHolder(View itemView) {
            super(itemView);
            item_ = (TextView)itemView.findViewById(R.id.item_his);
        }
    }
}
