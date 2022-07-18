package com.example.myapplication;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.CustomViewHolder> {

    private ArrayList<Todo> arrayList = new ArrayList<>();

    public TodoAdapter(ArrayList<Todo> arrayList)
    {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
       holder.tv_name=(TextView) holder.itemView.findViewById(R.id.tv_name);
       holder.checkBox=(CheckBox)holder.itemView.findViewById(R.id.checkbox);
       holder.deleteTodo=(Button) holder.itemView.findViewById(R.id.deleteTodo);
       holder.tv_name.setText(arrayList.get(position).getContent());
       holder.checkBox.setChecked(arrayList.get(position).isCompleted());
        holder.deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAdapterPosition());
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()==true){
                    holder.tv_name.setPaintFlags(holder.tv_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    holder.tv_name.setPaintFlags(0);
                }
                Log.d("check state",""+ holder.checkBox.isChecked());
                Todo todo = new Todo(arrayList.get(position).getContent(),holder.checkBox.isChecked());
                arrayList.set(position,todo);
            }
        });

    }
    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    public Todo getItemCount(int position) {
        return arrayList.get(position);
    }

    public class CustomViewHolder extends  RecyclerView.ViewHolder{

        protected TextView tv_name;
        protected CheckBox checkBox;
        protected Button deleteTodo;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            this.deleteTodo=(Button)itemView.findViewById(R.id.deleteTodo);
            this.checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);
        }

        public Todo getItem(int position) {
            return arrayList.get(position);
        }
    }
}
