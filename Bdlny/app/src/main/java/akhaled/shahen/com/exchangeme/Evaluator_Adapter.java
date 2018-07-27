package akhaled.shahen.com.exchangeme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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

import static android.content.Intent.ACTION_CALL;


public class Evaluator_Adapter extends RecyclerView.Adapter<Evaluator_Adapter.Hholder> {
    List<Evaluator_item> evaluator_items;
    Context context;
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;

    public Evaluator_Adapter(List<Evaluator_item> evaluator_items, Context context) {
        this.evaluator_items = evaluator_items;
        this.context = context;
    }

    @Override
    public Evaluator_Adapter.Hholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evaluator_item, parent, false);
        return new Hholder(v);
    }

    @Override
    public void onBindViewHolder(Evaluator_Adapter.Hholder holder, final int position) {
        final Evaluator_item evaluator_item = evaluator_items.get(position);
        holder.name.setText(evaluator_item.getName());


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                transaction = fragmentManager.beginTransaction();
//                newFragment = Home.newInstance( evaluator_item.getName() );
//                transaction.replace( R.id.swap_all, newFragment );
//                //this.getActionBar().setTitle("Home");
//                transaction.commit();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (position == 0) {
                    context.startActivity(new Intent(ACTION_CALL, Uri.parse("tel:" + "122")));
                }else if(position ==1){
                    context.startActivity(new Intent(ACTION_CALL, Uri.parse("tel:" + "123")));
                }else if(position ==2){
                    context.startActivity(new Intent(ACTION_CALL, Uri.parse("tel:" + "124")));
                }else if(position ==3){
                    context.startActivity(new Intent(ACTION_CALL, Uri.parse("tel:" + "125")));
                }else if(position ==4){
                    context.startActivity(new Intent(ACTION_CALL, Uri.parse("tel:" + "126")));
                }


            }
        } );

    }



    @Override
    public int getItemCount() {
        return evaluator_items.size();
    }

    public class Hholder extends RecyclerView.ViewHolder {
        TextView name;

        public Hholder(View itemView) {
            super( itemView );
            name = (TextView)itemView.findViewById( R.id.evaluator_item_name );

        }
    }
}
