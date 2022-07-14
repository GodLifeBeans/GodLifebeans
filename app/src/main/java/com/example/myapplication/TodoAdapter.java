package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.CustomViewHolder> {

    private ArrayList<Todo> arrayList;
    public TodoAdapter(ArrayList<Todo> arrayList)
    {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public TodoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.CustomViewHolder holder, int position) {
        holder.checkbox = (CheckBox)holder.itemView.findViewById(R.id.checkbox);
        holder.tv_name=(TextView) holder.itemView.findViewById(R.id.tv_name);
        holder.deleteTodo=(Button) holder.itemView.findViewById(R.id.deleteTodo);

        holder.tv_name.setText(arrayList.get(position).getContent());
        holder.checkbox.setChecked(arrayList.get(position).isCompleted());
        holder.deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("check","check");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends  RecyclerView.ViewHolder{

        protected TextView tv_name;
        protected Button deleteTodo;
        protected CheckBox checkbox;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            this.deleteTodo = (Button)itemView.findViewById(R.id.deleteTodo);
            this.checkbox = (CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }
}
