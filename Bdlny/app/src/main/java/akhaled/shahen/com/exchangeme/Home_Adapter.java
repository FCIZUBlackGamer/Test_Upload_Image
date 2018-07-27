package akhaled.shahen.com.exchangeme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fci on 04/03/18.
 */

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.Hholder> {
    List<Home_item> home_items;
    Context context;

    public Home_Adapter(List<Home_item> home_items, Context context) {
        this.home_items = home_items;
        this.context = context;
    }

    @Override
    public Home_Adapter.Hholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item,parent,false);
        return new Hholder(v);
    }
//get
    @Override
    public void onBindViewHolder(Home_Adapter.Hholder holder, int position) {
        final Home_item home_item = home_items.get( position );
        holder.name.setText( home_item.getName() );
        holder.desc.setText( home_item.getDiscription() );
        Picasso.with(context)
                .load(home_item.getImg())
                .into(holder.img);
        holder.name.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Result_Activity.class);
                intent.putExtra("capital",home_item.getCapital());
                intent.putExtra("From","Home");
                intent.putExtra("price",home_item.getPrice());
                intent.putExtra("user_email",home_item.getUser_email());
                intent.putExtra("product_name",home_item.getName());
                intent.putExtra("product_img",home_item.getImg());
                intent.putExtra("type",home_item.getType());
                intent.putExtra("description",home_item.getDiscription());
                intent.putExtra("phone",home_item.getPhone());
                intent.putExtra("date",home_item.getDate());
                context.startActivity( intent );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return home_items.size();
    }

    public class Hholder extends RecyclerView.ViewHolder {
        TextView name, desc;
        ImageView img;
        public Hholder(View itemView) {
            super( itemView );
            name = (TextView)itemView.findViewById( R.id.home_item_name );
            desc = (TextView)itemView.findViewById( R.id.home_item_desc );
            img = (ImageView)itemView.findViewById( R.id.home_item_img );
        }
    }
}
