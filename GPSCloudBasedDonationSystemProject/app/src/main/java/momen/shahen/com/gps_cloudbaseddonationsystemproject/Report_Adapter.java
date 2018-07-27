package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.*;
import java.util.List;

/**
 * Created by fci on 16/01/18.
 */

public class Report_Adapter extends RecyclerView.Adapter<Report_Adapter.ViewHolder> {
    private List<Report_item> reportItems;
    private Context context;

    public Report_Adapter(List<Report_item> reportItems, Context context) {
        this.reportItems = reportItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Report_item report_item = reportItems.get(position);

        holder.tim.setText(report_item.getTime());
        holder.nam.setText(report_item.getName());
        Picasso.with(context)
                .load(report_item.getPhoto_url())
                .into(holder.file);
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tim, nam;
        ImageView file;
        public ViewHolder(View itemView) {
            super(itemView);
            tim = (TextView)itemView.findViewById(R.id.rep_tim);
            nam = (TextView)itemView.findViewById(R.id.rep_hos_nam);
            file = (ImageView) itemView.findViewById(R.id.Image_report_item);
        }
    }
}
