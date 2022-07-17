package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeansAdapter extends RecyclerView.Adapter<BeansAdapter.CustomViewHolder> {

    private ArrayList<Beans> arrayList;

    public BeansAdapter(ArrayList<Beans> arrayList){
        this.arrayList=arrayList;
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
//        holder.beans_image.setImageResource(R.drawable.ic_launcher_foreground);
        holder.beans_price.setText(arrayList.get(position).getBeans_price());
        holder.beans_name.setText(arrayList.get(position).getBeans_name());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView beans_name;
        protected TextView beans_price;
//        protected ImageView beans_image;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
//            this.beans_image = (ImageView)itemView.findViewById(R.id.beans_image);
            this.beans_name = (TextView)itemView.findViewById(R.id.beans_name);
            this.beans_price = (TextView)itemView.findViewById(R.id.beans_price);
        }
    }
}
