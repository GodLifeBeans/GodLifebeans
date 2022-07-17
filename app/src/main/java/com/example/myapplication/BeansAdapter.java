package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeansAdapter extends RecyclerView.Adapter<BeansAdapter.CustomViewHolder> {

    private ArrayList<Beans> arrayList;
    //생성자 선언
    public BeansAdapter(ArrayList<Beans> arrayList ) {
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    //처음으로 생성될 때
    public BeansAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beans_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull BeansAdapter.CustomViewHolder holder, int position) {
        Log.d("holder", "------ holder = " + (holder.photoid));

        holder.photoid = (ImageView)holder.itemView.findViewById(R.id.photoid);
        holder.phonenum = (TextView)holder.itemView.findViewById(R.id.phonenum);
        holder.name = (TextView)holder.itemView.findViewById(R.id.name);


        holder.photoid.setImageResource(R.drawable.ic_launcher_foreground);
        holder.phonenum.setText(arrayList.get(position).getBeans_price());
        holder.name.setText(arrayList.get(position).getBeans_name());

        holder.itemView.setTag(position);


    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView photoid;
        protected TextView phonenum;
        protected TextView name;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            // Log.d("찾아야돼",itemView.findViewById(R.id.photoid).toString());
            this.photoid = (ImageView)itemView.findViewById(R.id.photoid);
            this.phonenum = (TextView)itemView.findViewById(R.id.phonenum);
            Log.d("찾아야돼",""+((ImageView)itemView.findViewById(R.id.photoid)==null));
            this.name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
