package akhaled.shahen.com.exchangeme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fci on 04/03/18.
 */

public class Department_Adapter extends RecyclerView.Adapter<Department_Adapter.Hholder> {
    List<Department_item> department_items;
    Context context;
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;

    public Department_Adapter(List<Department_item> department_items, Context context) {
        this.department_items = department_items;
        this.context = context;
    }

    @Override
    public Department_Adapter.Hholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_item,parent,false);
        return new Hholder(v);
    }

    @Override
    public void onBindViewHolder(final Department_Adapter.Hholder holder, int position) {
        final Department_item department_item = department_items.get( position );
        holder.name.setText( department_item.getName() );
        holder.img.setImageResource( department_item.getImg() );

        holder.name.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    newFragment = Home.newInstance( department_item.getName() );
                    transaction.replace( R.id.swap_all, newFragment );
                    //this.getActionBar().setTitle("Home");
                    transaction.commit();


            }
        } );
    }

    @Override
    public int getItemCount() {
        return department_items.size();
    }

    public class Hholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        public Hholder(View itemView) {
            super( itemView );
            name = (TextView)itemView.findViewById( R.id.dep_item_name );
            img = (ImageView)itemView.findViewById( R.id.dep_item_img );
        }
    }
}
